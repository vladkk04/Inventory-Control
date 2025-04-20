package com.example.bachelorwork.domain.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateResponse(
    @SerialName("user_id")
    val userId: String,
)