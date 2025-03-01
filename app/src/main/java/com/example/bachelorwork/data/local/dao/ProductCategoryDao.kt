package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao: BaseDao<ProductCategoryEntity> {

    @Query("SELECT * FROM product_categories")
    fun getAll(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM product_categories WHERE category_id = :id")
    fun getById(id: Int): Flow<ProductCategoryEntity>


}