package com.example.bachelorwork.ui.model

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.model.ProductUnit
import com.example.bachelorwork.domain.model.ProductTag

data class ProductCreateFormState(
    val name: String = "",
    val nameError: String? = null,
    val barcode: String = "",
    val barcodeError: String? = null,
    val quantity: Int = 1,
    val unit: ProductUnit = ProductUnit.PCS,
    val pricePerUnit: Double = 0.00,
    val pricePerUnitError: String? = null,
    val datePurchase: String = "",
    val datePurchaseError: String? = null,
    val minStockLevel: String = "",
    val minStockLevelError: String? = null,
    val category: ProductCategory? = null,
    val categoryError: String? = null,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
) {
    val totalPrice: Double
        get() = quantity * pricePerUnit
}
