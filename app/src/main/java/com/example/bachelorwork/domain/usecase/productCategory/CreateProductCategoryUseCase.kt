package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.data.local.entity.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.ProductCategoryRepository

class CreateProductCategoryUseCase (
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategoryEntity) = runCatching {
        productCategoryRepository.insert(productCategory)
    }

    suspend operator fun invoke(vararg productCategory: ProductCategoryEntity) = runCatching {
        productCategoryRepository.insertAll(*productCategory)
    }

}