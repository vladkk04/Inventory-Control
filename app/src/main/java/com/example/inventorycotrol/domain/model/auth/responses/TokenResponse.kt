package com.example.inventorycotrol.domain.model.auth.responses

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
