package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeStockProductDto(
    val id: String,
    @SerialName("previous_stock")
    val previousStock: Double,
    @SerialName("adjustment")
    val adjustmentValue: Double,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updateAt: Long,
)
