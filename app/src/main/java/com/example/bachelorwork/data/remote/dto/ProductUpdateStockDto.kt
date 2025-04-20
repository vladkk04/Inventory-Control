package com.example.bachelorwork.data.remote.dto

import com.example.bachelorwork.domain.model.updateStock.ProductStockUpdate
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateStockDto(
    val id: String,
    @SerialName("organisation_id")
    val organisationId: String,
    @SerialName("products")
    val productsUpdates: List<ProductStockUpdate>,
    @SerialName("operation_type")
    val stockOperationType: StockOperationType,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updateAt: Long,
)