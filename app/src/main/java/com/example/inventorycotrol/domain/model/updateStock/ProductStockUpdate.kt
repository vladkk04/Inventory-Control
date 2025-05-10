package com.example.inventorycotrol.domain.model.updateStock

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductStockUpdate(
    @SerialName("product_id")
    val productId: String,
    @SerialName("previous_stock")
    val previousStock: Double,
    @SerialName("adjustment")
    val adjustmentValue: Double,
)
