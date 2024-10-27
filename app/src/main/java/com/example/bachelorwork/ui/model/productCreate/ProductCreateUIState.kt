package com.example.bachelorwork.ui.model.productCreate

import com.example.bachelorwork.domain.model.ProductCategory

data class ProductCreateUIState(
    val categories: List<ProductCategory> = emptyList(),
    val isProductCreated: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

