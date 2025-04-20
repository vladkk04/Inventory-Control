package com.example.bachelorwork.data.remote.dto

import com.example.bachelorwork.domain.model.updateStock.ProductWithDetails
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateStockViewDto(
    val id: String,
    val products: List<ProductWithDetails>,
    @SerialName("operation_type")
    val operationType: StockOperationType,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updatedAt: Long
)
