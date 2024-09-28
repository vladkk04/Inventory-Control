package com.example.bachelorwork.domain.model

data class Product(
    val name: String,
    val category: String,
    val quantity: Int,
    val price: Double,
    val country: String,
    val code: Long
)