package com.example.bachelorwork.ui.model.productManage

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory

data class ProductManageUIState(
    val titleToolbar: String = "",
    val product: Product? = null,
    val categories: List<ProductCategory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {

}

