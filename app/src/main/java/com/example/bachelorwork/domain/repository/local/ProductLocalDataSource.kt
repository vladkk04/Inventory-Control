package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.ProductDetail
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource: BaseRoomRepository<ProductEntity> {

    fun getAll(): Flow<List<ProductEntity>>

    fun getById(id: String): Flow<ProductEntity>

    fun getByIdWithCategory(id: String): Flow<ProductDetail>

    fun getAllWithCategory(): Flow<List<ProductDetail>>

    suspend fun refresh(productRemote: List<ProductEntity>)

    suspend fun deleteById(id: String)
}
