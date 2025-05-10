package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.ProductCategoryDto
import com.example.inventorycotrol.domain.model.category.CreateProductCategoryResponse
import com.example.inventorycotrol.domain.model.category.ProductCategoryRequest
import kotlinx.coroutines.flow.Flow

interface ProductCategoryRemoteDataSource {

    suspend fun create(request: ProductCategoryRequest): Flow<ApiResponseResult<CreateProductCategoryResponse>>

    suspend fun update(categoryId: String, request: ProductCategoryRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(categoryId: String): Flow<ApiResponseResult<Unit>>

    suspend fun get(categoryId: String): Flow<ApiResponseResult<ProductCategoryDto>>

    fun getAll(): Flow<ApiResponseResult<List<ProductCategoryDto>>>

}