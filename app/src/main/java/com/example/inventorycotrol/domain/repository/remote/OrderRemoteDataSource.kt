package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.domain.model.order.OrderRequest
import kotlinx.coroutines.flow.Flow

interface OrderRemoteDataSource {

    suspend fun create(request: OrderRequest): Flow<ApiResponseResult<OrderDto>>

    suspend fun update(orderId: String, request: OrderRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(orderId: String): Flow<ApiResponseResult<Unit>>

    suspend fun get(orderId: String): Flow<ApiResponseResult<OrderDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<OrderDto>>>


}