package com.example.inventorycotrol.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)
