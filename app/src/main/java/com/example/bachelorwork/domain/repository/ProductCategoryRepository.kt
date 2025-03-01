package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface ProductCategoryRepository: BaseRoomRepository<ProductCategoryEntity> {

    fun getAll(): Flow<List<ProductCategoryEntity>>

    fun getById(id: Int): Flow<ProductCategoryEntity>
}