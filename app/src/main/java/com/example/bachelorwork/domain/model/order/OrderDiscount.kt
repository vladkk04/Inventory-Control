package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.ui.model.order.DiscountType
import kotlinx.serialization.Serializable

@Serializable
data class OrderDiscount(
    val value: Double,
    val type: DiscountType
)
