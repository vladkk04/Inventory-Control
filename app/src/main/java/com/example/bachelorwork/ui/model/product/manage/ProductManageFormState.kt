package com.example.bachelorwork.ui.model.product.manage

import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit

data class ProductManageFormState(
    val name: String = "",
    val nameError: String? = null,
    val barcode: String = "",
    val barcodeError: String? = null,
    val quantity: String = "",
    val quantityError: String? = null,
    val productUnit: ProductUnit = ProductUnit.PCS,
    val minStockLevel: String = "",
    val minStockLevelError: String? = null,
    val categoryId: String = "",
    val categoryError: String? = null,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
)

