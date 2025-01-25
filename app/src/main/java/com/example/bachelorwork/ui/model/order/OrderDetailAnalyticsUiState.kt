package com.example.bachelorwork.ui.model.order

import java.util.Date

data class OrderDetailAnalyticsUiState(
    val priceChartData: PriceChartData = PriceChartData(),
    val amountChartData: AmountChartData = AmountChartData(),
)

data class PriceChartData(
    val data: Map<Date, Double> = emptyMap(),
)

data class AmountChartData(
    val data: Map<Date, Double> = emptyMap(),
)