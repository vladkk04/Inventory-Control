package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class DeleteProductCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategory) = runCatching {
        productCategoryRepository.delete(productCategory)
    }

    suspend operator fun invoke(vararg productCategory: ProductCategory) = runCatching {
        productCategoryRepository.deleteAll(*productCategory)
    }
}