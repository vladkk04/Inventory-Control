package com.example.bachelorwork.ui.model.productList

import android.net.Uri

data class ProductListUI(
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val price: Double,
    val quantity: Int
)