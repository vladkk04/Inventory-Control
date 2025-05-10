package com.example.inventorycotrol.domain.model.updateStock

import kotlinx.serialization.SerialName

data class ProductStock(
    val id: String,
    @SerialName("organisation_id")
    val organisationId: String,
    @SerialName("products")
    val productsUpdates: List<ProductStockUpdate>,
    @SerialName("operation_type")
    val updatedBy: String,
    @SerialName("updated_at")
    val updateAt: Long,
)
