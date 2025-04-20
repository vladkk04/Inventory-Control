package com.example.bachelorwork.ui.model.order.product

data class OrderAddProductFormUiState(
    val quantity: String = "",
    val rate: String = "",
    val quantityError: String? = null,
    val rateError: String? = null
)
