package com.example.bachelorwork.domain.model.product

data class Product(
    val id: String,
    val imageUrl: String? = null,
    val name: String,
    val barcode: String,
    val quantity: Double,
    val unit: ProductUnit,
    val categoryName: String? = null,
    val minStockLevel: Double,
    val description: String? = null,
    val organisationId: String,
    val createdBy: String,
    val createdAt: Int,
    val tags: List<ProductTag> = emptyList(),
    val updates: List<ProductUpdateHistory> = emptyList()
)