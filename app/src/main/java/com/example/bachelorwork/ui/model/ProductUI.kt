package com.example.bachelorwork.ui.model

import android.net.Uri

data class ProductUI(
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val price: Double,
    val quantity: Int
)