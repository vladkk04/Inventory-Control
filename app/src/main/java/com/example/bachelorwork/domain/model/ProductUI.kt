package com.example.bachelorwork.domain.model

import android.net.Uri

data class ProductUI(
    val name: String,
    val image: Uri? = null,
    val barcode: String,
    val weight: Double,
    val price: Double,
    val quantity: Int
)