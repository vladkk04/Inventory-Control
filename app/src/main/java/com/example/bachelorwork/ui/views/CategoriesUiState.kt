package com.example.bachelorwork.ui.views

import com.example.bachelorwork.domain.model.product.ProductCategory

data class CategoriesUiState(
    val categories: List<ProductCategory> = emptyList(),
    val errorMessage: String? = null
)
