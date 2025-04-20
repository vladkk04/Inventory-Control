package com.example.bachelorwork.domain.model.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
    @SerialName("image_url")
    val imageUrl: String?,
    val name: String,
    val barcode: String,
    val quantity: Double,
    val unit: ProductUnit,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("min_stock_level")
    val minStockLevel: Double,
    val description: String?,
    val tags: List<ProductTag>,
)
