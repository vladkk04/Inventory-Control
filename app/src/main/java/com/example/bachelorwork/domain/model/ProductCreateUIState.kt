package com.example.bachelorwork.domain.model

data class ProductCreateUIState(
    val quantity: Int = 1,
    val price: Double = 0.00,
    val totalPrice: Double = 0.00,
    val datePurchase: String = "",
    val barcode: String? = null,
    val errorMessage: String? = null
) {
    operator fun inc(): ProductCreateUIState = copy(quantity = quantity.inc())
    operator fun dec(): ProductCreateUIState = copy(quantity = quantity.dec().coerceAtLeast(1))
}
