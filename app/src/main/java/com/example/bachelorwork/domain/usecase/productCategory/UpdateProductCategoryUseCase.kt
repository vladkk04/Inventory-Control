package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.data.local.entity.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.ProductCategoryRepository

class UpdateProductCategoryUseCase(
    private val categoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(category: ProductCategoryEntity) = runCatching {
        categoryRepository.update(category)
    }

    suspend operator fun invoke(vararg category: ProductCategoryEntity) = runCatching {
        categoryRepository.updateAll(*category)
    }
}