package com.example.bachelorwork.domain.model.updateStock

import com.example.bachelorwork.domain.model.product.ProductUnit
import kotlinx.serialization.Serializable

@Serializable
data class ProductInUpdateStock(
    val id: String,
    val name: String,
    val unit: ProductUnit,
    val previousStock: Double,
    val adjustmentValue: Double
)
