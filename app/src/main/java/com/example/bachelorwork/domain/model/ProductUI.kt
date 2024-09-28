package com.example.bachelorwork.domain.model

import android.net.Uri

data class ProductUI(
    val name: String,
    val image: Uri? = null,
    val upcCode: String,
    val price: Double,
    val weight: Double,
    val quantity: Int
)