package com.example.bachelorwork.ui.model.order.product

import com.example.bachelorwork.ui.model.order.SelectableProductUi

data class OrderAddProductUiState(
    val products: List<SelectableProductUi> = emptyList(),
    val pinnedProduct: SelectableProductUi? = null,
    val isLoading: Boolean = false,
    val canAddProduct: Boolean = false,
)
