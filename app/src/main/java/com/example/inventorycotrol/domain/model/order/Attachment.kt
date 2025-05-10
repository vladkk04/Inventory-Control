package com.example.inventorycotrol.domain.model.order

import kotlinx.serialization.Serializable


@Serializable
data class Attachment(
    val url: String,
    val size: String,
)
