package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.ui.model.order.DiscountType
import java.util.Date

data class Order(
    val id: Int,
    val total: Double,
    val discount: Double,
    val discountType: DiscountType,
    val orderedAt: Date,
    val orderedBy: String,
)

/*
fun Order.toOrderEntity(): OrderEntity = OrderEntity(
    id = this.id,
    total = this.total,
    orderedAt = this.orderedAt,
    orderedBy = this.orderedBy
)
*/


