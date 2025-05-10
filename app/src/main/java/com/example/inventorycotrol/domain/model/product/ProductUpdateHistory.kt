package com.example.inventorycotrol.domain.model.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductUpdateHistory(
    @SerialName("update_at")
    val updatedAt: Long,
    @SerialName("update_by")
    val updatedBy: String
)






