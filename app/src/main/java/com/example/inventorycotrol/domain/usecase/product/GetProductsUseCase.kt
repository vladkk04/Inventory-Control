package com.example.inventorycotrol.domain.usecase.product

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class GetProductsUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {
    suspend fun getById(productId: String): Flow<Resource<Product>> {
        return when (val response = remote.get(productId).last()) {
            ApiResponseResult.Loading -> flowOf(Resource.Loading)
            is ApiResponseResult.Failure -> {
                val localData = local.getByIdWithCategory(productId).first()
                flowOf(
                    Resource.Error(
                        localData?.product?.mapToDomain()?.copy(
                            categoryName = localData.category?.name
                        ), response.errorMessage
                    )
                )
            }

            is ApiResponseResult.Success -> {
                //local.upsert(response.data.mapToEntity())
                val result = local.getByIdWithCategory(productId).map { productDetail ->
                    Resource.Success(
                        productDetail?.product?.mapToDomain()?.copy(
                            categoryName = productDetail.category?.name
                        ) ?: response.data.mapToDomain()
                    )
                }
                return result
            }
        }
    }

    suspend fun getAll(): Flow<Resource<List<Product>>> {
        return when (val response = remote.getAll().last()) {
            ApiResponseResult.Loading -> flowOf(Resource.Loading)
            is ApiResponseResult.Failure -> {
                val localData = local.getAllWithCategory().first()
                val result = localData.map { productDetail ->
                    productDetail.product.mapToDomain().copy(
                        categoryName = productDetail.category?.name
                    )
                }
                flowOf(Resource.Error(result, response.errorMessage))
            }

            is ApiResponseResult.Success -> {
                local.refresh(response.data.map { it.mapToEntity() })
                val result = local.getAllWithCategory().map { productDetail ->
                    Resource.Success(productDetail.map {
                        it.product.mapToDomain().copy(
                            categoryName = it.category?.name
                        )
                    })
                }
                return result
            }
        }
    }/* remote.getAll().flattingRemoteToLocal(
            onFailureBlock = { errorMessage ->
                local.getAllWithCategory().map { entities ->
                    val list = entities.map { productDetail ->
                        productDetail.product.mapToDomain().copy(
                            categoryName = productDetail.category?.name
                        )
                    }
                    Resource.Error(data = list, errorMessage = errorMessage)
                }
            },
            onSuccessBlock = { response ->
                local.refresh(response.map { it.mapToEntity() })
                local.getAllWithCategory().map { entities ->
                    val list = entities.map { productDetail ->
                        productDetail.product.mapToDomain().copy(
                            categoryName = productDetail.category?.name
                        )
                    }
                    Resource.Success(list)
                }
            }
        )*/

}
