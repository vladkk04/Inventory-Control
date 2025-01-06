package com.example.bachelorwork.ui.model.product.productList

import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.ProductDisplayMode

data class ProductListUIState(
    val products: List<ProductUI> = emptyList(),
    val viewType: ProductDisplayMode = ProductDisplayMode.ROW,
    val orderBy: ProductSortOptions = ProductSortOptions(),
    val isNoProducts: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
