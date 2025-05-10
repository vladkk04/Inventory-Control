package com.example.inventorycotrol.ui.fragments.warehouse.productDetail.analytics

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.ChangeStockProductDto
import com.example.inventorycotrol.domain.model.TimePeriod
import com.example.inventorycotrol.domain.usecase.order.OrderUseCases
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.ui.model.order.detail.OrderDetailAnalyticsUiState
import com.example.inventorycotrol.ui.model.order.detail.StockChangeChartData
import com.example.inventorycotrol.ui.model.order.detail.StockVolumeChartData
import com.example.inventorycotrol.ui.model.productUpdateStock.StockHistory
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductDetailAnalyticsViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
    private val productStockUpdate: ProductUpdateStockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailAnalyticsUiState())
    val uiState get() = _uiState.asStateFlow()

    private val productRouteArg = Destination.from<Destination.ProductDetail>(savedStateHandle)

    init {
        getChartsDate()
    }

    fun changeStockDatePeriod(period: TimePeriod) {
        _uiState.update { it.copy(
            datePeriodChangeStock = period,
            datePeriodVolumeStock = period
        ) }
        getChartsDate()
    }


    fun openSelectorPeriodDate() = viewModelScope.launch {
        navigator.navigate(Destination.SelectDatePeriod)
    }

    private fun getChartsDate() = viewModelScope.launch {
        productStockUpdate.getStock.getAllByProductId(productRouteArg.id).collectLatest { result ->
            when (result) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ApiResponseResult.Failure -> {

                }
                is ApiResponseResult.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@collectLatest
                    }

                    val allTransactions = result.data.sortedBy { it.updateAt }
                    val filteredTransactions = filterTransactions(allTransactions, _uiState.value.datePeriodChangeStock)
                    if (filteredTransactions.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@collectLatest
                    }

                    val initialStock = getInitialStockForPeriod(allTransactions, _uiState.value.datePeriodChangeStock)
                    val stockHistory = buildStockHistory(filteredTransactions, initialStock)
                    val chartData = createChartData(stockHistory, _uiState.value.datePeriodChangeStock)

                    val volumeData = createVolumeData(filteredTransactions)

                    _uiState.update {
                        it.copy(
                            stockChangeChartData = StockChangeChartData(chartData),
                            volumeStockChartData = volumeData,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    private fun getInitialStockForPeriod(
        allTransactions: List<ChangeStockProductDto>,
        period: TimePeriod
    ): Double {
        val periodStart = when (period) {
            TimePeriod.TODAY -> getStartOfDay(0)
            TimePeriod.LAST_3_DAYS -> getStartOfDay(3)
            TimePeriod.LAST_7_DAYS -> getStartOfDay(7)
            TimePeriod.LAST_MONTH -> getStartOfDay(30)
            TimePeriod.LAST_90_DAYS -> getStartOfDay(90)
            TimePeriod.LAST_YEAR -> getStartOfDay(365)
        }

        return allTransactions
            .filter { it.updateAt < periodStart }
            .maxByOrNull { it.updateAt }
            ?.let { lastTx -> lastTx.previousStock + lastTx.adjustmentValue
            } ?: allTransactions.first().previousStock
    }

    private fun createChartData(
        stockHistory: StockHistory,
        period: TimePeriod
    ): Map<String, Double> {
        val periodStart = when (period) {
            TimePeriod.TODAY -> getStartOfDay(0)
            TimePeriod.LAST_3_DAYS -> getStartOfDay(3)
            TimePeriod.LAST_7_DAYS -> getStartOfDay(7)
            TimePeriod.LAST_MONTH -> getStartOfDay(30)
            TimePeriod.LAST_YEAR -> getStartOfDay(365)
            TimePeriod.LAST_90_DAYS -> getStartOfDay(90)
        }

        val chartEntries = mutableMapOf<String, Double>().apply {
            put(periodStart.toString(), stockHistory.initialStock)

            stockHistory.changes.forEach { change ->
                put(change.timestamp.toString(), change.newStock)
            }
        }

        return chartEntries.toSortedMap()
    }

    private fun getStartOfDay(daysAgo: Int): Long {
        return Instant.now().atZone(ZoneId.systemDefault())
            .minusDays(daysAgo.toLong())
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun buildStockHistory(
        transactions: List<ChangeStockProductDto>,
        initialStock: Double
    ): StockHistory {
        val sortedTx = transactions.sortedBy { it.updateAt }
        val changes = mutableListOf<StockHistory.StockChange>()
        var currentStock = initialStock

        for (tx in sortedTx) {
            val newStock = currentStock + tx.adjustmentValue
            changes.add(
                StockHistory.StockChange(
                    previousStock = currentStock,
                    adjustment = tx.adjustmentValue,
                    newStock = newStock,
                    timestamp = tx.updateAt
                )
            )
            currentStock = newStock
        }

        return StockHistory(initialStock, changes)
    }


    private fun createVolumeData(
        transactions: List<ChangeStockProductDto>,
    ): StockVolumeChartData {
        val (startMillis, endMillis) = getPeriodRange(_uiState.value.datePeriodVolumeStock)

        Log.d("debug", _uiState.value.datePeriodVolumeStock.toString())
        val periodTransactions = transactions.filter { it.updateAt in startMillis..endMillis }

        // Calculate totals for the entire period
        var totalIn = 0.0
        var totalOut = 0.0

        periodTransactions.forEach { tx ->
            if (tx.adjustmentValue >= 0) {
                totalIn += tx.adjustmentValue
            } else {
                totalOut += -tx.adjustmentValue
            }
        }

        return StockVolumeChartData(
            x = listOf(startMillis, endMillis),
            y = mapOf(
                "Stock in" to listOf(totalIn),
                "Stock out" to listOf(totalOut)
            )
        )
    }

    private fun formatDate(millis: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(millis))
    }

    private fun getPeriodRange(period: TimePeriod): Pair<Long, Long> {
        val now = System.currentTimeMillis()
        return when (period) {
            TimePeriod.TODAY -> {
                val startOfDay = Instant.now().atZone(ZoneId.systemDefault())
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(startOfDay, now)
            }
            TimePeriod.LAST_3_DAYS -> {
                val start = Instant.now().atZone(ZoneId.systemDefault())
                    .minusDays(3)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(start, now)
            }
            TimePeriod.LAST_7_DAYS -> {
                val start = Instant.now().atZone(ZoneId.systemDefault())
                    .minusDays(7)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(start, now)
            }
            TimePeriod.LAST_MONTH -> {
                val start = Instant.now().atZone(ZoneId.systemDefault())
                    .minusDays(30)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(start, now)
            }
            TimePeriod.LAST_90_DAYS -> {
                val start = Instant.now().atZone(ZoneId.systemDefault())
                    .minusDays(90)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(start, now)
            }
            TimePeriod.LAST_YEAR -> {
                val start = Instant.now().atZone(ZoneId.systemDefault())
                    .minusDays(365)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
                Pair(start, now)
            }
        }
    }

    private fun filterTransactions(
        transactions: List<ChangeStockProductDto>,
        period: TimePeriod
    ): List<ChangeStockProductDto> {
        val now = System.currentTimeMillis()
        val startTime = when (period) {
            TimePeriod.TODAY -> Instant.now().atZone(ZoneId.systemDefault())
                .toLocalDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()

            TimePeriod.LAST_3_DAYS -> now - TimeUnit.DAYS.toMillis(3)
            TimePeriod.LAST_7_DAYS -> now - TimeUnit.DAYS.toMillis(7)
            TimePeriod.LAST_MONTH -> now - TimeUnit.DAYS.toMillis(30)
            TimePeriod.LAST_90_DAYS -> now - TimeUnit.DAYS.toMillis(90)
            TimePeriod.LAST_YEAR -> now - TimeUnit.DAYS.toMillis(365)
        }
        return transactions
            .filter { it.updateAt in startTime..now }
            .sortedByDescending { it.updateAt }
    }
}
