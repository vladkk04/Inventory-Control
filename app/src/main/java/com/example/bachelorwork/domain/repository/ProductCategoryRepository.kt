package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.domain.model.ProductCategory

interface ProductCategoryRepository {

    suspend fun createCategory(name: String): Result<ProductCategory>

    suspend fun updateCategory(category: ProductCategory): Result<ProductCategory>

    suspend fun deleteCategory(category: ProductCategory)

    suspend fun getCategories(): Result<List<ProductCategory>>
}