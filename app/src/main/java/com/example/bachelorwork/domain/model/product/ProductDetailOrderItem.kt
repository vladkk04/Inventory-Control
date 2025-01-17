package com.example.bachelorwork.domain.model.product

import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import java.util.Date

data class ProductDetailOrderItem(
    val orderId: Int,
    val orderedAt: Date,
    val orderProductSubItem: OrderProductSubItem,
)
