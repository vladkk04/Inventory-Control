package com.example.bachelorwork.ui.fragments.orderList.create

import com.example.bachelorwork.ui.model.OrderSelectableProductUi

data class OrderAddProductUiState(
    val searchQuery: String = "",
    val products: List<OrderSelectableProductUi> = emptyList(),
)
