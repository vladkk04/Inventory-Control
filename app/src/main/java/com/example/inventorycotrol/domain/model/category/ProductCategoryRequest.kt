package com.example.inventorycotrol.domain.model.category
import kotlinx.serialization.Serializable

@Serializable
data class ProductCategoryRequest(
    val name: String
)