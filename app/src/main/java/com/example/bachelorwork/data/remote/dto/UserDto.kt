package com.example.bachelorwork.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("registered_at")
    val registeredAt: Long
)