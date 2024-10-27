package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import javax.inject.Inject

class GetProductCategoriesUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(): Result<List<ProductCategory>> {
        return productCategoryRepository.getCategories()
    }

}