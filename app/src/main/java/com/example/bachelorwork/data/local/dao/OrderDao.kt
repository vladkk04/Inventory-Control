package com.example.bachelorwork.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.order.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    @Query("SELECT * FROM orders")
    fun getAll(): PagingSource<Int, OrderEntity>

    @Query("SELECT * FROM orders WHERE orders.order_id = :id")
    fun getById(id: Int): Flow<OrderEntity>

}