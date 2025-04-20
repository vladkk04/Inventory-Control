package com.example.bachelorwork.domain.model.organisation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationRequest(
    val name: String,
    val currency: String,
    val description: String,
    @SerialName("logo_url")
    val logoUrl: String? = null,
)
