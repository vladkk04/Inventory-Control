package com.example.inventorycotrol.data.remote.dto

import com.example.inventorycotrol.domain.model.product.ProductTag
import com.example.inventorycotrol.domain.model.product.ProductUnit
import com.example.inventorycotrol.domain.model.product.ProductUpdateHistory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val name: String,
    val barcode: String,
    val quantity: Double,
    val unit: ProductUnit,
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("min_stock_level")
    val minStockLevel: Double,
    val description: String? = null,
    @SerialName("organisation_id")
    val organisationId: String,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("created_at")
    val createdAt: Long,
    val tags: List<ProductTag>,
    val updates: List<ProductUpdateHistory>
)

