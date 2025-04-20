package com.example.bachelorwork.domain.model.updateStock

import com.example.bachelorwork.domain.model.product.ProductUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductWithDetails(
    @SerialName("product_id")
    val productId: String,
    val name: String,
    val unit: ProductUnit,
    @SerialName("previous_stock")
    val previousStock: Double,
    @SerialName("adjustment")
    val adjustmentValue: Double
)
