package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.base.BaseDao
import com.example.bachelorwork.domain.model.product.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao: BaseDao<Product> {

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<Array<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<Product>
}