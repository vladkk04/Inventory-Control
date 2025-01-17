package com.example.bachelorwork.ui.model.product.detail

import com.example.bachelorwork.domain.model.product.Product

data class ProductDetailUIState(
    val product: Product? = null,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
