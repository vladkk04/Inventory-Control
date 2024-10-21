package com.example.bachelorwork.domain.usecase.category

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val productCategoryRepository: ProductCategoryRepository
) {
    suspend operator fun invoke(name: String): Flow<Result<ProductCategory>> = flow {
        productCategoryRepository.createCategory(name).mapCatching { result ->
            emit(Result.success(result))
        }.onFailure { error ->
            emit(Result.failure(error))
        }
    }
}