package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.base.BaseDao
import com.example.bachelorwork.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao: BaseDao<OrderEntity> {

    @Query("SELECT * FROM orders")
    fun getAll(): Flow<Array<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orders.id = :id")
    fun getById(id: Int): Flow<OrderEntity>

}