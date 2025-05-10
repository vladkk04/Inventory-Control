package com.example.inventorycotrol.ui.model.productUpdateStock

data class StockHistory(
    val initialStock: Double,
    val changes: List<StockChange>
) {
    data class StockChange(
        val previousStock: Double,
        val adjustment: Double,
        val newStock: Double,
        val timestamp: Long
    )
}