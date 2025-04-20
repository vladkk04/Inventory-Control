package com.example.bachelorwork.ui.fragments.warehouse.productDetail.analytics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.ChangeStockProductDto
import com.example.bachelorwork.domain.model.TimePeriod
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import com.example.bachelorwork.ui.model.order.detail.OrderDetailAnalyticsUiState
import com.example.bachelorwork.ui.model.order.detail.StockChangeChartData
import com.example.bachelorwork.ui.model.order.detail.StockVolumeChartData
import com.example.bachelorwork.ui.model.productUpdateStock.StockHistory
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
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
        _uiState.update { it.copy(datePeriodChangeStock = period) }
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

                    val volumeData = createVolumeData(filteredTransactions, _uiState.value.datePeriodVolumeStock)

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
            ?.let { lastTx ->
                when (lastTx.stockOperationType) {
                    StockOperationType.STOCK_IN -> lastTx.previousStock + lastTx.adjustmentValue
                    StockOperationType.STOCK_OUT -> lastTx.previousStock - lastTx.adjustmentValue
                }
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
            val newStock = when (tx.stockOperationType) {
                StockOperationType.STOCK_IN -> currentStock + tx.adjustmentValue
                StockOperationType.STOCK_OUT -> currentStock - tx.adjustmentValue
            }
            changes.add(
                StockHistory.StockChange(
                    previousStock = currentStock,
                    adjustment = tx.adjustmentValue,
                    newStock = newStock,
                    operationType = tx.stockOperationType,
                    timestamp = tx.updateAt
                )
            )
            currentStock = newStock
        }

        return StockHistory(initialStock, changes)
    }


    private fun createVolumeData(
        transactions: List<ChangeStockProductDto>,
        period: TimePeriod
    ): StockVolumeChartData {
        val groupedTransactions = when (period) {
            TimePeriod.TODAY -> {

                val last24hTransactions = transactions.filter {
                    Instant.ofEpochMilli(it.updateAt).isAfter(Instant.now().minus(24, ChronoUnit.HOURS))
                }

                mapOf(Instant.now().toEpochMilli() to last24hTransactions)
            }

            TimePeriod.LAST_3_DAYS,
            TimePeriod.LAST_7_DAYS -> transactions.groupBy {
                Instant.ofEpochMilli(it.updateAt)
                    .atZone(ZoneId.systemDefault())
                    .truncatedTo(ChronoUnit.DAYS)
                    .toInstant()
                    .toEpochMilli()
            }

            TimePeriod.LAST_MONTH,
            TimePeriod.LAST_90_DAYS -> transactions.groupBy {
                Instant.ofEpochMilli(it.updateAt)
                    .atZone(ZoneId.systemDefault())
                    .truncatedTo(ChronoUnit.MONTHS)
                    .toInstant()
                    .toEpochMilli()
            }

            TimePeriod.LAST_YEAR -> transactions.groupBy {
                Instant.ofEpochMilli(it.updateAt)
                    .atZone(ZoneId.systemDefault())
                    .withDayOfMonth(1)
                    .truncatedTo(ChronoUnit.YEARS)
                    .toInstant()
                    .toEpochMilli()
            }
        }
        val stockIn = mutableListOf<Double>()
        val stockOut = mutableListOf<Double>()
        val timestamps = groupedTransactions.keys.sorted()

        timestamps.forEach { timestamp ->
            val periodTransactions = groupedTransactions[timestamp] ?: emptyList()
            var inVolume = 0.0
            var outVolume = 0.0

            periodTransactions.forEach { tx ->
                when (tx.stockOperationType) {
                    StockOperationType.STOCK_IN -> inVolume += tx.adjustmentValue
                    StockOperationType.STOCK_OUT -> outVolume += tx.adjustmentValue
                }
            }

            stockIn.add(inVolume)
            stockOut.add(outVolume)
        }

        return StockVolumeChartData(
            x = timestamps,
            y = mapOf(
                "Stock in" to stockIn,
                "Stock out" to stockOut
            )
        )
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
