package com.example.bachelorwork.ui.model.productCreate

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag

data class ProductCreateFormState(
    val name: String = "",
    val nameError: String? = null,
    val barcode: String = "",
    val barcodeError: String? = null,
    val quantity: Int = 1,
    val productUnit: Product.ProductUnit = Product.ProductUnit.PCS,
    val pricePerUnit: String = "",
    val pricePerUnitError: String? = null,
    val datePurchase: String = "",
    val datePurchaseError: String? = null,
    val minStockLevel: String = "",
    val minStockLevelError: String? = null,
    val category: ProductCategory = ProductCategory(name = ""),
    val categoryError: String? = null,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
) {
    val totalPrice: Double
        get() = quantity * (pricePerUnit.toDoubleOrNull() ?: 0.00)

    fun increaseQuantity() = this.copy(quantity = quantity + 1)
    fun decreaseQuantity() = this.copy(quantity = (quantity - 1).coerceAtLeast(1))
}
