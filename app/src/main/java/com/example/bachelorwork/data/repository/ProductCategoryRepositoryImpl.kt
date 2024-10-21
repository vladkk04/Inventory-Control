package com.example.bachelorwork.data.repository

import com.example.bachelorwork.domain.model.DefaultCategory
import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject


class ProductCategoryRepositoryImpl @Inject constructor() : ProductCategoryRepository {

    override suspend fun createCategory(name: String): Result<ProductCategory> {
        return Result.success(ProductCategory(name))
    }

    override suspend fun deleteCategory(category: ProductCategory) {

    }

    override suspend fun getAllCategories(): Result<List<ProductCategory>> {
        return Result.success(DefaultCategory.entries.map {
            it.toCategory()
        })
    }
}