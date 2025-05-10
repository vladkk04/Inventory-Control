package com.example.inventorycotrol.ui.model.order

import com.example.inventorycotrol.domain.model.product.ProductUnit

data class OrderProductUi(
    val id: String,
    val imageUrl: String? = null,
    val name: String,
    val quantity: Double,
    val price: Double,
    val unit: ProductUnit
)
