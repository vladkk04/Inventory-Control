package com.example.bachelorwork.ui.model.product.productDetail

import com.example.bachelorwork.domain.model.product.Product

data class ProductDetailUIState(
    val product: Product? = null,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
