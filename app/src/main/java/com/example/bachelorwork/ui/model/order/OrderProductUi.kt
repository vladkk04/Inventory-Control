package com.example.bachelorwork.ui.model.order

import com.example.bachelorwork.domain.model.product.ProductUnit

data class OrderProductUi(
    val id: String,
    val imageUrl: String? = null,
    val name: String,
    val quantity: Double,
    val price: Double,
    val unit: ProductUnit
)
