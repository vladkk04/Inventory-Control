package com.example.bachelorwork.ui.model.product.productManage

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.ui.model.product.productDetail.ProductDetailUIState

data class ProductEditUIState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)