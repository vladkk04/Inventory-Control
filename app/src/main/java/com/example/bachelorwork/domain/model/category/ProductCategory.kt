package com.example.bachelorwork.domain.model.category

import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory(
    val id: String,
    val name: String,
)
