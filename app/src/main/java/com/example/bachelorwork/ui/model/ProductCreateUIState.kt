package com.example.bachelorwork.ui.model

import com.example.bachelorwork.domain.model.ProductCategory
import com.google.mlkit.vision.barcode.common.Barcode

data class ProductCreateUIState(
    val categories: List<ProductCategory> = emptyList(),
    val isProductCreated: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)