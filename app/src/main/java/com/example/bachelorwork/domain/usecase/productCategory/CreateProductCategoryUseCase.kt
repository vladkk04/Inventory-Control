package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class CreateProductCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategory) = runCatching {
        productCategoryRepository.insert(productCategory)
    }

    suspend operator fun invoke(vararg productCategory: ProductCategory) = runCatching {
        productCategoryRepository.insertAll(*productCategory)
    }

}