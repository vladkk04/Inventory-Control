package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.ProductDetail
import com.example.bachelorwork.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao: BaseDao<ProductEntity> {

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE products.id = :id")
    fun getById(id: String): Flow<ProductEntity>

    @Query("DELETE FROM products WHERE products.id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM products WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Transaction
    suspend fun refresh(remoteProducts: List<ProductEntity>) {
        val remoteIds = remoteProducts.map { it.id }
        deleteExcept(remoteIds)
        upsertAll(*remoteProducts.toTypedArray())
    }

    @Transaction
    @Query("SELECT * FROM products WHERE products.id = :id")
    fun getByIdWithCategory(id: String): Flow<ProductDetail>

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsWithCategory(): Flow<List<ProductDetail>>

}