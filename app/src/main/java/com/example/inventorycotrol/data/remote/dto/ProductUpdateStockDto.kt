package com.example.inventorycotrol.data.remote.dto

import com.example.inventorycotrol.domain.model.updateStock.ProductStockUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateStockDto(
    val id: String,
    @SerialName("organisation_id")
    val organisationId: String,
    @SerialName("products")
    val productsUpdates: List<ProductStockUpdate>,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updateAt: Long,
)