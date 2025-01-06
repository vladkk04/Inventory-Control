package com.example.bachelorwork.ui.fragments.orders.create

import com.example.bachelorwork.ui.model.order.OrderAddedProductUi

data class OrderCreateUiState(
    val addedProduct: List<OrderAddedProductUi> = emptyList()
)
