package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationDto(
    val id: String,
    val name: String,
    val currency: String,
    val description: String,
    @SerialName("logo_url")
    val logoUrl: String? = null,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("created_at")
    val createdAt: Long,
)
