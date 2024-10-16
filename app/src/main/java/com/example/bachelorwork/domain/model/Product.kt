package com.example.bachelorwork.domain.model

import android.net.Uri

data class Product(
    val name: String,
    val image: Uri? = null,
    val category: String,
    val quantity: Int,
    val weight: Double,
    val price: Double,
)