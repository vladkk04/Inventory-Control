package com.example.bachelorwork.domain.model.order

import kotlinx.serialization.Serializable


@Serializable
data class Attachment(
    val url: String,
    val size: String,
)
