package com.example.bachelorwork.domain.usecase.category

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(): Result<List<ProductCategory>> {
        return productCategoryRepository.getAllCategories()
    }

}