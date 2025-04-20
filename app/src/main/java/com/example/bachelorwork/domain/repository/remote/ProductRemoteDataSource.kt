package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.ProductDto
import com.example.bachelorwork.domain.model.product.ProductRequest
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {

    suspend fun create(request: ProductRequest): Flow<ApiResponseResult<ProductDto>>

    suspend fun update(productId: String, request: ProductRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(productId: String): Flow<ApiResponseResult<Unit>>

    suspend fun get(productId: String): Flow<ApiResponseResult<ProductDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<ProductDto>>>

}