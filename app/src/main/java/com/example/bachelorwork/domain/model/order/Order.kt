package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.data.local.entity.OrderEntity
import java.util.Date

data class Order(
    val id: Int,
    val total: Double,
    val orderedAt: Date,
    val orderedBy: String,
)

fun Order.toOrderEntity(): OrderEntity = OrderEntity(
    id = this.id,
    total = this.total,
    orderedAt = this.orderedAt,
    orderedBy = this.orderedBy
)


