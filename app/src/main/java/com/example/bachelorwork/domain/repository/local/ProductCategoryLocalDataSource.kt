package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface ProductCategoryLocalDataSource: BaseRoomRepository<ProductCategoryEntity> {

    fun getAll(): Flow<List<ProductCategoryEntity>>

    fun getById(id: String): Flow<ProductCategoryEntity>

    suspend fun refresh(categories: List<ProductCategoryEntity>)

    suspend fun deleteById(id: String)
}