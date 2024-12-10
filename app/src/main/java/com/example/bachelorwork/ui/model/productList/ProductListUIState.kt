package com.example.bachelorwork.ui.model.productList

import com.example.bachelorwork.domain.model.product.ProductOrder
import com.example.bachelorwork.domain.model.product.ProductViewType

data class ProductListUIState(
    val products: List<ProductListItemUI> = emptyList(),
    val viewType: ProductViewType = ProductViewType.ROW,
    val orderBy: ProductOrder = ProductOrder(),
    val isNoProducts: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
