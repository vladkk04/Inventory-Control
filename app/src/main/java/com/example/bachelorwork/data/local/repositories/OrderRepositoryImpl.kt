package com.example.bachelorwork.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bachelorwork.data.local.dao.OrderDao
import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImpl(
    private val dao: OrderDao,
) : OrderRepository {

    override fun getAll(): Flow<PagingData<OrderEntity>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { dao.getAll() }
    ).flow

    override fun getById(id: Int): Flow<OrderEntity> = dao.getById(id)

    override suspend fun insert(obj: OrderEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: OrderEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: OrderEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: OrderEntity) = dao.updateAll(*obj)

    override suspend fun delete(obj: OrderEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: OrderEntity) = dao.deleteAll(*obj)
}