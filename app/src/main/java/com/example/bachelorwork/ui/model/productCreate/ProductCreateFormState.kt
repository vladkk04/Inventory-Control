package com.example.bachelorwork.ui.model.productCreate

data class ProductCreateFormState(
    val name: String = "",
    val nameError: String? = null,
    val barcode: String = "",
    val barcodeError: String? = null,
    val quantity: Int = 1,
    val unit: String = "",
    val pricePerUnit: String = "",
    val pricePerUnitError: String? = null,
    val datePurchase: String = "",
    val datePurchaseError: String? = null,
    val minStockLevel: String = "",
    val minStockLevelError: String? = null,
    val category: String = "",
    val categoryError: String? = null,
    val tags: List<String> = emptyList(),
    val description: String = "",
) {
    val totalPrice: Double
        get() = quantity * (pricePerUnit.toDoubleOrNull() ?: 0.00)

    operator fun inc() = this.copy(quantity = quantity + 1)
    operator fun dec() = this.copy(quantity = (quantity - 1).coerceAtLeast(1))
}
