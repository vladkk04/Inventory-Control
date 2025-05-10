package com.example.inventorycotrol.domain.usecase.productCategory

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import com.example.inventorycotrol.domain.model.category.ProductCategoryRequest
import com.example.inventorycotrol.domain.repository.local.ProductCategoryLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductCategoryRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull

class CreateProductCategoryUseCase(
    private val local: ProductCategoryLocalDataSource,
    private val remote: ProductCategoryRemoteDataSource
) {
    operator fun invoke(request: ProductCategoryRequest): Flow<Resource<Unit>> = flow {
        remote.create(request).lastOrNull()?.let { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    emit(Resource.Loading)
                }

                is ApiResponseResult.Failure -> {
                    emit(Resource.Error<Unit>(errorMessage = response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    val entity = ProductCategoryEntity(
                        id = response.data.id,
                        name = request.name
                    )
                    local.insert(entity)
                    emit(Resource.Success(Unit))
                }
            }
        }
    }.catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }


}