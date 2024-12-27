package com.example.bachelorwork.ui.model.productManage

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.ui.model.productDetail.ProductDetailUIState

data class ProductEditUIState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)