package com.example.bachelorwork.domain.model.category
import kotlinx.serialization.Serializable

@Serializable
data class ProductCategoryRequest(
    val name: String
)