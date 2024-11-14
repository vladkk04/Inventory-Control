package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductCategoriesUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    operator fun invoke() = productCategoryRepository.getAll().map { categories ->
        runCatching { categories.map { it.copy(name = it.name.lowercase().replaceFirstChar(Char::titlecase)) } }
    }
}