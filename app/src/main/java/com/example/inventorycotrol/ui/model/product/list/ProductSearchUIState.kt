package com.example.inventorycotrol.ui.model.product.list

import com.example.inventorycotrol.domain.model.product.Product

data class ProductSearchUIState(
    val products: List<Product> = emptyList(),
)