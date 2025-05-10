package com.example.inventorycotrol.domain.model.category

import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory(
    val id: String,
    val name: String,
)
