package com.example.bachelorwork.domain.model

import android.net.Uri

data class Product(
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val pricePerUnit: ProductUnit,
    val totalPrice: Double,
    val datePurchase: String,
    val minStockLevel: Int,
    val category: ProductCategory,
    val tags: List<ProductTag>,
    val description: String,
)