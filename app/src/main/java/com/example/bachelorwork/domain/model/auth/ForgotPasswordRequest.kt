package com.example.bachelorwork.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email: String
)
