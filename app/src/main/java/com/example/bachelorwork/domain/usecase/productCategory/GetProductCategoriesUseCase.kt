package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.mappers.mapToDomain
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.model.category.ProductCategory
import com.example.bachelorwork.domain.repository.local.ProductCategoryLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductCategoryRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetProductCategoriesUseCase(
    private val remote: ProductCategoryRemoteDataSource,
    private val local: ProductCategoryLocalDataSource
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<Resource<List<ProductCategory>>> {
        return remote.getAll()
            .flatMapLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> flowOf(Resource.Loading)
                    is ApiResponseResult.Failure -> flowOf(Resource.Error(errorMessage = response.errorMessage))
                    is ApiResponseResult.Success -> {
                        local.refresh(response.data.map { it.mapToEntity() })
                        local.getAll().map { entities ->
                            Resource.Success(entities.map { it.mapToDomain() })
                        }
                    }
                }
            }
            .onStart { emit(Resource.Loading) }
            .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
    }
}