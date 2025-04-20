package com.example.bachelorwork.data.local.repositories

import com.example.bachelorwork.data.local.dao.OrderDao
import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.data.local.entities.OrderProductJoin
import com.example.bachelorwork.data.local.entities.RawOrderData
import com.example.bachelorwork.data.remote.dto.OrderDto
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import kotlinx.coroutines.flow.Flow

class OrderLocalDataSourceImpl(
    private val dao: OrderDao,
) : OrderLocalDataSource {

    override fun getAll(): Flow<List<RawOrderData>> = dao.getAll()

    override suspend fun refresh(orderRemote: List<OrderDto>) = dao.refresh(orderRemote.map { it.mapToEntity() })

    override suspend fun getById(id: String): List<RawOrderData> = dao.getRawOrderData(id)

    override suspend fun insertOrderDetail(orderProductJoin: OrderProductJoin) = dao.insert(orderProductJoin)

    override suspend fun insert(obj: OrderEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: OrderEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: OrderEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: OrderEntity) = dao.updateAll(*obj)

    override suspend fun upsert(obj: OrderEntity) = dao.upsert(obj)

    override suspend fun upsertAll(vararg obj: OrderEntity) = dao.upsertAll(*obj)

    override suspend fun delete(obj: OrderEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: OrderEntity) = dao.deleteAll(*obj)
    
}