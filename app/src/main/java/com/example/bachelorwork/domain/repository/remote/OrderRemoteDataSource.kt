package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.OrderDto
import com.example.bachelorwork.domain.model.order.OrderRequest
import kotlinx.coroutines.flow.Flow

interface OrderRemoteDataSource {

    suspend fun create(request: OrderRequest): Flow<ApiResponseResult<OrderDto>>

    suspend fun update(orderId: String, request: OrderRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(orderId: String): Flow<ApiResponseResult<Unit>>

    suspend fun get(orderId: String): Flow<ApiResponseResult<OrderDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<OrderDto>>>


}