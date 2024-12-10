package com.example.bachelorwork.ui.model.productList

data class ProductSearchUIState(
    val products: List<ProductListItemUI> = emptyList(),
    val isNoItemsFound: Boolean = false
)