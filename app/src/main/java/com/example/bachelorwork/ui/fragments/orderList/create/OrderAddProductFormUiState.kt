package com.example.bachelorwork.ui.fragments.orderList.create

data class OrderAddProductFormUiState(
    val quantity: String = "",
    val rate: String = "",
    val quantityError: String? = null,
    val rateError: String? = null
)
