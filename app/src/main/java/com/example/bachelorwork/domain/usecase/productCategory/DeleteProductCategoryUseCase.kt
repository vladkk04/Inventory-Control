package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class DeleteProductCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategory) {
        productCategoryRepository.deleteCategory(productCategory)
    }

}