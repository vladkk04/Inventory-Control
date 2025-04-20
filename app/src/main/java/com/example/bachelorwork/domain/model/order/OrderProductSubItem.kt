package com.example.bachelorwork.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderProductSubItem(
    val id: String,
    val image: String? = null,
    val name: String,
    val unit: String,
    val price: Double,
    val quantity: Double
)
