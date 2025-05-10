package com.example.inventorycotrol.ui.fragments.productUpdateStock

import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockItem


data class ProductUpdateStockUiState(
    val products: List<ProductUpdateStockItem> = emptyList(),
    val isLoading: Boolean = true
)