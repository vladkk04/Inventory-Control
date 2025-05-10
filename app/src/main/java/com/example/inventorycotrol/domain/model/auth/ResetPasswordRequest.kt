package com.example.inventorycotrol.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val password: String,
    val token: String
)
