package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository

class UpdateProductCategoryUseCase(
    private val categoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(category: ProductCategory) = runCatching {
        categoryRepository.update(category)
    }

    suspend operator fun invoke(vararg category: ProductCategory) = runCatching {
        categoryRepository.updateAll(*category)
    }
}