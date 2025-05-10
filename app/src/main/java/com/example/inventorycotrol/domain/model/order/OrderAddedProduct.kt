package com.example.inventorycotrol.domain.model.order

import com.example.inventorycotrol.domain.model.product.ProductUnit

data class OrderAddedProduct(
    val id: String,
    val name: String,
    val image: String? = null,
    val previousStock: Double,
    val unit: ProductUnit,
    val price: Double,
    val quantity: Double,
    val total: Double
)
