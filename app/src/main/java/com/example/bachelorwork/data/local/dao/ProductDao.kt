package com.example.bachelorwork.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao: BaseDao<ProductEntity> {

    @Query("SELECT * FROM products")
    fun getAll(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE products.product_id = :id")
    fun getById(id: Int): Flow<ProductEntity>

   /* @Transaction
    @Query("SELECT * FROM products")
    fun getProductsDetails(): Flow<List<ProductDetails>>

    @Transaction
    @Query("SELECT * FROM products WHERE products.product_id = :id")
    fun getProductDetails(id: Int): Flow<ProductDetails>*/
}