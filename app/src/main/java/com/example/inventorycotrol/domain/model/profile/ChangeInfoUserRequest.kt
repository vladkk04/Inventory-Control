package com.example.inventorycotrol.domain.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeInfoUserRequest(
    @SerialName("full_name")
    val fullName: String,
    @SerialName("logo_url")
    val logoUrl: String?
)
