package com.example.inventorycotrol.ui.model.product.detail

import com.example.inventorycotrol.ui.model.product.detail.orders.ProductDetailOrderUi

data class ProductDetailOrdersUiState(
    val orders: List<ProductDetailOrderUi> = emptyList(),
    val noOrders: Boolean = orders.isEmpty()
)
