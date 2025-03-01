package com.example.bachelorwork.ui.model.order.product

import com.example.bachelorwork.ui.model.order.OrderSearchableProductListUi

data class OrderManageProductUiState(
    val products: List<OrderSearchableProductListUi> = emptyList(),
    val isSelectedProduct: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
