package com.example.bachelorwork.ui.model.productCreate

import com.example.bachelorwork.domain.model.product.ProductCategory

data class ProductCreateUIState(
    val isProductCreated: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val barcode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

