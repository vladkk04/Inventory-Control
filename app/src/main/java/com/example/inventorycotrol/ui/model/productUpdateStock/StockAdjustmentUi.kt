package com.example.inventorycotrol.ui.model.productUpdateStock

data class StockAdjustmentUi(
    val productId: String,
    val imageUrl: String?,
    val productName: String,
    val currentStock: Double,
    val adjustedAmount: Double,
    val unit: String
)
