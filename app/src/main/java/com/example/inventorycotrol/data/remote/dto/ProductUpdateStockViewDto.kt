package com.example.inventorycotrol.data.remote.dto

import com.example.inventorycotrol.domain.model.updateStock.ProductWithDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateStockViewDto(
    val id: String,
    val products: List<ProductWithDetails>,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updatedAt: Long
)
