package com.example.bachelorwork.ui.model.productUpdateStock

import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType

data class StockHistory(
    val initialStock: Double,
    val changes: List<StockChange>
) {
    data class StockChange(
        val previousStock: Double,
        val adjustment: Double,
        val newStock: Double,
        val operationType: StockOperationType,
        val timestamp: Long
    )
}