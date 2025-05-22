package com.example.inventorycotrol.ui.model.productUpdateStock

import com.example.inventorycotrol.domain.model.product.ProductUnit

data class ProductUpdateStockItem(
    val id: String,
    val name: String,
    val stockOnHand: Double,
    val minStockLevel: Double,
    val unit: ProductUnit,
    val currentInputValue: Double = stockOnHand
) {
    val adjustmentAmount: Double
        get() = currentInputValue - stockOnHand
}
