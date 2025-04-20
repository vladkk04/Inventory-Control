package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.mappers.mapToDomain
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.local.ProductLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetProductsUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getById(productId: String): Flow<Resource<Product>> {
        return remote.get(productId).flatMapLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> flowOf(Resource.Loading)
                is ApiResponseResult.Failure -> {
                    flowOf(Resource.Error(errorMessage = response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    local.update(response.data.mapToEntity())
                    local.getByIdWithCategory(productId).map {
                        Resource.Success(
                            it.product.mapToDomain().copy(
                                categoryName = it.category.name
                            )
                        )
                    }
                }
            }.onStart { emit(Resource.Loading) }
                .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getAll(): Flow<Resource<List<Product>>> {
        return remote.getAll()
            .flatMapLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> flowOf(Resource.Loading)
                    is ApiResponseResult.Failure -> {
                        flowOf(Resource.Error(errorMessage = response.errorMessage))
                    }
                    is ApiResponseResult.Success -> {
                        local.refresh(response.data.map { it.mapToEntity() })
                        local.getAllWithCategory().map { entities ->
                            val list = entities.map { productDetail ->
                                productDetail.product.mapToDomain().copy(
                                    categoryName = productDetail.category.name
                                )
                            }

                            Resource.Success(list)
                        }
                    }
                }
            }
            .onStart { emit(Resource.Loading) }
            .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
    }

}
