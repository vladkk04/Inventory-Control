package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.data.local.entity.toProductCategory
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductCategoriesUseCase(
    private val productCategoryRepository: ProductCategoryRepository
) {
    operator fun invoke(): Flow<Result<List<ProductCategory>>> =
        productCategoryRepository.getAll().map { categories ->
            runCatching { categories.map { it.toProductCategory() } }
        }
}