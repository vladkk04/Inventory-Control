package com.example.inventorycotrol.domain.repository.local

import com.example.inventorycotrol.data.local.entities.OrderEntity
import com.example.inventorycotrol.data.local.entities.OrderProductJoin
import com.example.inventorycotrol.data.local.entities.RawOrderData
import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrderLocalDataSource: BaseRoomRepository<OrderEntity> {

    fun getAll(): Flow<List<RawOrderData>>

    suspend fun deleteById(orderId: String)

    suspend fun refresh(orderRemote: List<OrderDto>)

    suspend fun getById(id: String): List<RawOrderData>

    suspend fun insertOrderDetail(orderProductJoin: OrderProductJoin)

    suspend fun upsertOrderJoin(vararg orderProductJoin: OrderProductJoin)
}
