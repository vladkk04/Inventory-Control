package com.example.bachelorwork.ui.model.category

import com.example.bachelorwork.domain.model.category.ProductCategory

data class CategoryUiState(
    val categories: List<ProductCategory> = emptyList(),
    val currentCategory: ProductCategory? = null,
    val errorMessage: String? = null
)
