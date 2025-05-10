package com.example.inventorycotrol.domain.model.order

import com.example.inventorycotrol.ui.model.order.DiscountType
import kotlinx.serialization.Serializable

@Serializable
data class OrderDiscount(
    val value: Double,
    val type: DiscountType
)
