package com.example.bachelorwork.ui.model.product.manage.edit

import com.example.bachelorwork.domain.model.product.Product

data class ProductEditUIState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)