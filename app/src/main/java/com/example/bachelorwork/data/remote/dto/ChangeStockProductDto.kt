package com.example.bachelorwork.data.remote.dto

import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeStockProductDto(
    val id: String,
    @SerialName("previous_stock")
    val previousStock: Double,
    @SerialName("adjustment")
    val adjustmentValue: Double,
    @SerialName("operation_type")
    val stockOperationType: StockOperationType,
    @SerialName("updated_by")
    val updatedBy: String,
    @SerialName("updated_at")
    val updateAt: Long,
)
