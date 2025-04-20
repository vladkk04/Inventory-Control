package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.data.local.entities.OrderProductJoin
import com.example.bachelorwork.data.local.entities.RawOrderData
import com.example.bachelorwork.data.remote.dto.OrderDto
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrderLocalDataSource: BaseRoomRepository<OrderEntity> {

    fun getAll(): Flow<List<RawOrderData>>

    suspend fun refresh(orderRemote: List<OrderDto>)

    suspend fun getById(id: String): List<RawOrderData>

    suspend fun insertOrderDetail(orderProductJoin: OrderProductJoin)
}
