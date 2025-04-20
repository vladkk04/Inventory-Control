package com.example.bachelorwork.ui.model.product.list

import com.example.bachelorwork.domain.model.product.Product

data class ProductSearchUIState(
    val products: List<Product> = emptyList(),
)