package com.example.inventorycotrol.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventorycotrol.data.local.dao.common.BaseDao
import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao: BaseDao<ProductCategoryEntity> {

    @Query("SELECT * FROM product_categories")
    fun getAll(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM product_categories WHERE id = :id")
    fun getById(id: String): Flow<ProductCategoryEntity>

    @Query("DELETE FROM product_categories WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM product_categories WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Transaction
    suspend fun refresh(categories: List<ProductCategoryEntity>) {
        deleteExcept(categories.map { it.id })
        upsertAll(*categories.toTypedArray())
    }

    @Query("DELETE FROM product_categories")
    suspend fun clearAll()


}