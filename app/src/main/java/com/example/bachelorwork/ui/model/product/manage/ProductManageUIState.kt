package com.example.bachelorwork.ui.model.product.manage

data class ProductManageUIState(
    val barcode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

