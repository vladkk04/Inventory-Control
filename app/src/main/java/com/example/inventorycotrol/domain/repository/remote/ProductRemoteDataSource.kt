package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.ProductDto
import com.example.inventorycotrol.domain.model.product.ProductRequest
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {

    suspend fun create(request: ProductRequest): Flow<ApiResponseResult<ProductDto>>

    suspend fun update(productId: String, request: ProductRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(productId: String): Flow<ApiResponseResult<Unit>>

    suspend fun get(productId: String): Flow<ApiResponseResult<ProductDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<ProductDto>>>

}