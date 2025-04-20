package com.example.bachelorwork.ui.model.order.detail

import com.example.bachelorwork.domain.model.TimePeriod

data class OrderDetailAnalyticsUiState(
    val stockChangeChartData: StockChangeChartData = StockChangeChartData(),
    val volumeStockChartData: StockVolumeChartData = StockVolumeChartData(),
    val datePeriodChangeStock: TimePeriod = TimePeriod.TODAY,
    val datePeriodVolumeStock: TimePeriod = TimePeriod.TODAY,
    val isLoading: Boolean = false,
)

data class StockChangeChartData(
    val data: Map<String, Double> = emptyMap(),
)

data class StockVolumeChartData(
    val x: List<Long> = emptyList(),
    val y: Map<String, List<Double>> = emptyMap(),
)