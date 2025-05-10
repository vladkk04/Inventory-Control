package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoleDto(
    val id: String,
    val name: String,
    @SerialName("organisation_id")
    val organisationId: String,
    val description: String,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("created_at")
    val createdAt: Int
)