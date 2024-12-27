package com.example.bachelorwork.ui.model.productManage

import com.example.bachelorwork.domain.model.product.ProductCategory

data class ProductCreateUIState(
    val barcode: String = "",
    val categories: List<ProductCategory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

