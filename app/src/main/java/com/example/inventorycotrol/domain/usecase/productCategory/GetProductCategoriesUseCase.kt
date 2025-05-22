package com.example.inventorycotrol.domain.usecase.productCategory

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.category.ProductCategory
import com.example.inventorycotrol.domain.repository.local.ProductCategoryLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductCategoryRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class GetProductCategoriesUseCase(
    private val remote: ProductCategoryRemoteDataSource,
    private val local: ProductCategoryLocalDataSource
) {

    /*@OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<Resource<List<ProductCategory>>> {
        return remote.getAll()
            .flatMapMerge { response ->
                when (response) {
                    ApiResponseResult.Loading -> flowOf(Resource.Loading)
                    is ApiResponseResult.Failure -> flowOf(Resource.Error(errorMessage = response.errorMessage))
                    is ApiResponseResult.Success -> {
                        local.upsertAll(*response.data.map { it.mapToEntity() }.toTypedArray())
                        local.getAll().map { entities ->
                            Resource.Success(entities.map { it.mapToDomain() })
                        }
                    }
                }
            }
            .onStart { emit(Resource.Loading) }
            .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
    }*/




    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<Resource<List<ProductCategory>>> {
        val remoteFlow = flow {
            val remoteData = remote.getAll().last()
            when (remoteData) {
                ApiResponseResult.Loading -> {
                    emit(Resource.Loading)
                }

                is ApiResponseResult.Failure -> {
                    emit(Resource.Error(errorMessage = remoteData.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    local.refresh(remoteData.data.map { it.mapToEntity() })
                    emit(Resource.Success(remoteData.data.map { it.mapToDomain() }))
                }
            }
        }

        val localFlow = local.getAll().map { entities ->
            Resource.Success(entities.map { it.mapToDomain() })
        }

        return flowOf(localFlow, remoteFlow).flattenMerge()
    }
}