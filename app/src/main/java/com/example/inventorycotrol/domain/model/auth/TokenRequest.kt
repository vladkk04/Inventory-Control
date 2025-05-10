package com.example.inventorycotrol.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String
)
