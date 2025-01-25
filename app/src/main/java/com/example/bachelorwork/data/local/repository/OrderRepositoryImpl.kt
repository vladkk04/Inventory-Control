package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.dao.OrderDao
import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImpl(
    private val orderDao: OrderDao,
): OrderRepository {

    override fun getAll(): Flow<Array<OrderEntity>> = orderDao.getAll()

    override fun getById(id: Int): Flow<OrderEntity> = orderDao.getById(id)

    override suspend fun deleteByOrderId(id: Int) = orderDao.deleteByOrderId(id)

    override suspend fun insert(obj: OrderEntity) = orderDao.insert(obj)

    override suspend fun insertAll(vararg obj: OrderEntity) = orderDao.insertAll(*obj)

    override suspend fun update(obj: OrderEntity) = orderDao.update(obj)

    override suspend fun updateAll(vararg obj: OrderEntity) =  orderDao.updateAll(*obj)

    override suspend fun delete(obj: OrderEntity) = orderDao.delete(obj)

    override suspend fun deleteAll(vararg obj: OrderEntity) = orderDao.deleteAll(*obj)
}