package com.example.inventorycotrol.ui.model.category

import com.example.inventorycotrol.domain.model.category.ProductCategory

data class CategoryUiState(
    val categories: List<ProductCategory> = emptyList(),
    val currentCategory: ProductCategory? = null,
    val errorMessage: String? = null
)
