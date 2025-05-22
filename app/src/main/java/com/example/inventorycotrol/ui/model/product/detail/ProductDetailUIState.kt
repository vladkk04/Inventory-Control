package com.example.inventorycotrol.ui.model.product.detail

import com.example.inventorycotrol.domain.model.product.Product

data class ProductDetailUIState(
    val product: Product? = null,
    val isLoading: Boolean = false,
)
