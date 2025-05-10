package com.example.inventorycotrol.ui.model.order.product

import com.example.inventorycotrol.ui.model.order.SelectableProductUi

data class OrderAddProductUiState(
    val products: List<SelectableProductUi> = emptyList(),
    val pinnedProduct: SelectableProductUi? = null,
    val isLoading: Boolean = false,
    val currency: String = "",
    val canAddProduct: Boolean = false,
)
