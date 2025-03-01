package com.example.bachelorwork.ui.model.product.detail.orders

import java.util.Date

data class ProductDetailOrderUi(
    val name: String,
    val orderId: Int,
    val orderedAt: Date,
    val quantity: Double,
    val price: Double,
    val unit: String,
)
