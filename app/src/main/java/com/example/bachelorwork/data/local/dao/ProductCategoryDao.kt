package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.base.BaseDao
import com.example.bachelorwork.domain.model.product.ProductCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao: BaseDao<ProductCategory> {
    @Query("SELECT * FROM product_categories")
    fun getProductCategories(): Flow<Array<ProductCategory>>
}