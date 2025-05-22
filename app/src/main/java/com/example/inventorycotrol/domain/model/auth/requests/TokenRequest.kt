package com.example.inventorycotrol.domain.model.auth.requests

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String
)
