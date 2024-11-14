package com.example.bachelorwork.ui.model.productList

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductViewType
import com.example.bachelorwork.domain.model.product.ProductOrder

data class ProductListUIState(
    val products: List<Product> = emptyList(),
    val viewType: ProductViewType = ProductViewType.ROW,
    val orderBy: ProductOrder = ProductOrder(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
