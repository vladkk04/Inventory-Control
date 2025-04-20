package com.example.bachelorwork.ui.model.home

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.model.productUpdateStock.ProductUpdateStockCompact

data class HomeUiState(
    val criticalStockItems: List<Product> = emptyList(),
    val productUpdateStockItems: List<ProductUpdateStockCompact> = emptyList(),
    val totalUsersCount: Int = 0,
    val totalProductsCount: Int = 0,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)
