package com.example.bachelorwork.ui.model.product.detail

import com.example.bachelorwork.domain.model.product.ProductDetailOrderItem

data class ProductDetailOrdersUiState(
    val orders: List<ProductDetailOrderItem> = emptyList(),
    val noOrders: Boolean = orders.isEmpty()
)
