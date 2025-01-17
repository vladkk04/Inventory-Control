package com.example.bachelorwork.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderProductSubItem(
    val id: Int,
    val name: String,
    val unit: String,
    val price: Double,
    val quantity: Double
)
