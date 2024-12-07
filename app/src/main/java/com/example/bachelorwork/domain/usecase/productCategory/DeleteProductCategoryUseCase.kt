package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.ProductCategoryRepository

class DeleteProductCategoryUseCase (
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategoryEntity) = runCatching {
        productCategoryRepository.delete(productCategory)
    }

    suspend operator fun invoke(vararg productCategory: ProductCategoryEntity) = runCatching {
        productCategoryRepository.deleteAll(*productCategory)
    }
}