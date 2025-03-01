package com.example.bachelorwork.domain.repository

import androidx.paging.PagingData
import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrderRepository: BaseRoomRepository<OrderEntity> {

    fun getAll(): Flow<PagingData<OrderEntity>>

    fun getById(id: Int): Flow<OrderEntity>
}
