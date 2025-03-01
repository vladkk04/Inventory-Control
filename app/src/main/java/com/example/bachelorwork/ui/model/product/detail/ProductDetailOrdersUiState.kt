package com.example.bachelorwork.ui.model.product.detail

import com.example.bachelorwork.ui.model.product.detail.orders.ProductDetailOrderUi

data class ProductDetailOrdersUiState(
    val orders: List<ProductDetailOrderUi> = emptyList(),
    val noOrders: Boolean = orders.isEmpty()
)
