package com.example.bachelorwork.ui.model.product.productList

data class ProductSearchUIState(
    val products: List<ProductUI> = emptyList(),
    val isNoItemsFound: Boolean = false
)