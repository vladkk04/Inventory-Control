package com.example.inventorycotrol.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    val email: String,
    val otp: String
)
