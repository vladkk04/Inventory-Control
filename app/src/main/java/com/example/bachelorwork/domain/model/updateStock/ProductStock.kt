package com.example.bachelorwork.domain.model.updateStock

import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import kotlinx.serialization.SerialName

data class ProductStock(
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
