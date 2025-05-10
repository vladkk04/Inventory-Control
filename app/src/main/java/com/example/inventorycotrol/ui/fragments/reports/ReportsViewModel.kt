package com.example.inventorycotrol.ui.fragments.reports

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.ProductUpdateStockViewDto
import com.example.inventorycotrol.domain.model.TimePeriodReports
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.utils.ProductPdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val updateStockUseCases: ProductUpdateStockUseCases,
    private val organisationUseCases: OrganisationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState = _uiState.asStateFlow()

    private var updateStocks: List<ProductUpdateStockViewDto> = emptyList()

    private var customStartMillis: Long? = null
    private var customEndMillis: Long? = null

    init {
        getAllStocks()
    }


    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun getAllStocks() = viewModelScope.launch {
        updateStockUseCases.getStock.getAllByOrganisationView().onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {

                }

                is ApiResponseResult.Success -> {
                    updateStocks = response.data

                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getReportData(): List<ProductPdf> {
        val filteredData = filterByDateRange(
            updateStocks,
            uiState.value.timePeriod,
            customStartMillis,
            customEndMillis
        )
        return generateReport(filteredData)
    }

    private fun generateReport(filteredData: List<ProductUpdateStockViewDto>): List<ProductPdf> {
        return filteredData
            .flatMap { stockUpdate ->
                stockUpdate.products.map { product ->
                    Triple(
                        product.name,
                        product.previousStock,
                        product.adjustmentValue to product.adjustmentValue
                    )
                }
            }
            .groupBy { it.first }
            .map { (name, entries) ->
                val initialStock = entries.first().second
                val (totalIn, totalOut) = entries.fold(0.0 to 0.0) { (accIn, accOut), (_, _, adjustments) ->
                    accIn + adjustments.first to accOut + adjustments.second
                }

                ProductPdf(
                    name = name,
                    qtyIn = totalIn,
                    qtyOut = totalOut,
                    stockOnHand = initialStock + totalIn - totalOut
                )
            }
    }

    private fun filterByDateRange(
        allData: List<ProductUpdateStockViewDto>,
        timePeriod: TimePeriodReports,
        customStartMillis: Long? = null,
        customEndMillis: Long? = null
    ): List<ProductUpdateStockViewDto> {
        val now = LocalDate.now()

        val (startDate, endDate) = when (timePeriod) {
            TimePeriodReports.TODAY -> now to now
            TimePeriodReports.YESTERDAY -> now.minusDays(1) to now.minusDays(1)
            TimePeriodReports.THIS_WEEK -> now.with(DayOfWeek.MONDAY) to now
            TimePeriodReports.PREVIOUS_WEEK -> now.minusWeeks(1).with(DayOfWeek.MONDAY) to
                    now.minusWeeks(1).with(DayOfWeek.SUNDAY)

            TimePeriodReports.THIS_MONTH -> now.withDayOfMonth(1) to now
            TimePeriodReports.PREVIOUS_MONTH -> now.minusMonths(1).withDayOfMonth(1) to
                    now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth())

            TimePeriodReports.THIS_YEAR -> now.withDayOfYear(1) to now
            TimePeriodReports.PREVIOUS_YEAR -> now.minusYears(1).withDayOfYear(1) to
                    now.minusYears(1).withDayOfYear(now.minusYears(1).lengthOfYear())

            TimePeriodReports.CUSTOM -> {
                requireNotNull(customStartMillis) { "Custom start date must not be null" }.let { start ->
                    requireNotNull(customEndMillis) { "Custom end date must not be null" }.let { end ->
                        millisToLocalDate(start) to millisToLocalDate(end)
                    }
                }
            }

        }

        return allData.filter { item ->
            val itemDate =
                Instant.ofEpochMilli(item.updatedAt).atZone(ZoneId.systemDefault()).toLocalDate()
            !itemDate.isBefore(startDate) && !itemDate.isAfter(endDate)
        }
    }

    fun onTimePeriodSelected(timePeriodReports: TimePeriodReports) {
        _uiState.update { it.copy(timePeriod = timePeriodReports) }
    }

    fun setCustomDateRange(customRange: Pair<Long, Long>) {
        customStartMillis = customRange.first
        customEndMillis = customRange.second
        _uiState.update { it.copy(timePeriod = TimePeriodReports.CUSTOM) }
    }

    fun getDateRangeString(): String {
        val (startDate, endDate) = when (uiState.value.timePeriod) {
            TimePeriodReports.CUSTOM -> {
                millisToLocalDate(customStartMillis ?: 0L) to millisToLocalDate(
                    customEndMillis ?: 0L
                )
            }

            else -> {
                val now = LocalDate.now()
                when (uiState.value.timePeriod) {
                    TimePeriodReports.TODAY -> now to now
                    TimePeriodReports.YESTERDAY -> now.minusDays(1) to now.minusDays(1)
                    TimePeriodReports.THIS_WEEK -> now.with(DayOfWeek.MONDAY) to now
                    TimePeriodReports.PREVIOUS_WEEK -> now.minusWeeks(1).with(DayOfWeek.MONDAY) to
                            now.minusWeeks(1).with(DayOfWeek.SUNDAY)

                    TimePeriodReports.THIS_MONTH -> now.withDayOfMonth(1) to now
                    TimePeriodReports.PREVIOUS_MONTH -> now.minusMonths(1).withDayOfMonth(1) to
                            now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth())

                    TimePeriodReports.THIS_YEAR -> now.withDayOfYear(1) to now
                    TimePeriodReports.PREVIOUS_YEAR -> now.minusYears(1).withDayOfYear(1) to
                            now.minusYears(1).withDayOfYear(now.minusYears(1).lengthOfYear())

                    TimePeriodReports.CUSTOM -> throw IllegalStateException("Already handled")
                }
            }
        }

        return if (startDate == endDate) startDate.toString() else "$startDate - $endDate"
    }

    private fun millisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }


}