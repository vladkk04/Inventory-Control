package com.example.bachelorwork.ui.model.order.create.manage.product

import com.example.bachelorwork.ui.model.order.OrderSelectableProductListUi

data class OrderManageProductUiState(
    val products: List<OrderSelectableProductListUi> = emptyList(),
    val isSelectedProduct: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
