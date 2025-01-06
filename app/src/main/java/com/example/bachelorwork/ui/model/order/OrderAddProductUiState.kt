package com.example.bachelorwork.ui.model.order

data class OrderAddProductUiState(
    val searchQuery: String = "",
    val products: List<OrderSelectableProductUi> = emptyList(),
    val selectedProductId: Int? = null
)
