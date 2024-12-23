package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorwork.data.local.dao.base.BaseDao
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.data.local.pojo.ProductPojo
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao: BaseDao<ProductEntity> {

    @Query("SELECT * FROM products")
    fun getAll(): Flow<Array<ProductEntity>>

    @Query("SELECT * FROM products WHERE products.id = :id")
    fun getById(id: Int): Flow<ProductEntity>

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsPojo(): Flow<List<ProductPojo>>

    @Transaction
    @Query("SELECT * FROM products WHERE products.id = :id")
    fun getProductPojo(id: Int): Flow<ProductPojo>

    @Query("DELETE FROM products WHERE products.id = :id")
    suspend fun deleteByProductId(id: Int)
}