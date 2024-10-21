package com.example.bachelorwork.domain.usecase.category

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(productCategory: ProductCategory) {
        productCategoryRepository.deleteCategory(productCategory)
    }

}