package com.example.bachelorwork.ui.model.product.list

import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.ProductViewDisplayMode

data class ProductListUIState(
    val products: List<ProductUi> = emptyList(),
    val filtersCount: Int = 0,
    val viewDisplayType: ProductViewDisplayMode = ProductViewDisplayMode.ROW,
    val sortOptions: ProductSortOptions = ProductSortOptions(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

