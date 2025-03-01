package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.data.local.mappers.mapToDomain
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductCategoriesUseCase(
    private val repository: ProductCategoryRepository
) {
    operator fun invoke(): Flow<Result<List<ProductCategory>>> = repository.getAll().map {
        runCatching { it.map { it.mapToDomain() } }
    }
}