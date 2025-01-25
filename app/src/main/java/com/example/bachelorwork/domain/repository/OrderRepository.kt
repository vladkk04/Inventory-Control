package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface OrderRepository: BaseRepository<OrderEntity> {

    fun getAll(): Flow<Array<OrderEntity>>

    fun getById(id: Int): Flow<OrderEntity>

    suspend fun deleteByOrderId(id: Int)

}