package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.base.BaseDao
import com.example.bachelorwork.data.local.entity.ProductCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao: BaseDao<ProductCategoryEntity> {

    @Query("SELECT * FROM product_categories")
    fun getProductCategories(): Flow<Array<ProductCategoryEntity>>

}