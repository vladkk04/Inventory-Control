package com.example.bachelorwork.ui.model.category

import com.example.bachelorwork.domain.model.product.ProductCategory

data class CategoryUiState(
    val categories: List<ProductCategory> = emptyList(),
    val currentCategory: ProductCategory? = null,
    val errorMessage: String? = null
)
