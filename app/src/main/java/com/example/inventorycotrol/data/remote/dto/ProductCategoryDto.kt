package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductCategoryDto(
    val id: String,
    val name: String,
    @SerialName("organisation_id")
    val organisationId: String,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_by")
    val updatedBy: String?,
    @SerialName("updated_at")
    val updatedAt: Long?,
)
