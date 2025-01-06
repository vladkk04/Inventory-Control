package com.example.bachelorwork.ui.model.product.productManage

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit

data class ProductManageFormState(
    val name: String = "",
    val nameError: String? = null,
    val barcode: String = "",
    val barcodeError: String? = null,
    val quantity: Int = 1,
    val productUnit: ProductUnit = ProductUnit.PCS,
    val minStockLevel: String = "",
    val minStockLevelError: String? = null,
    val category: ProductCategory = ProductCategory(name = ""),
    val categoryError: String? = null,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
) {
    fun increaseQuantity() = this.copy(quantity = quantity + 1)
    fun decreaseQuantity() = this.copy(quantity = (quantity - 1).coerceAtLeast(1))
}

