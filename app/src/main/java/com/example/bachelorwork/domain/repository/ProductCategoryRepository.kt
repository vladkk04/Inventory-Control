package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface ProductCategoryRepository: BaseRepository<ProductCategoryEntity> {

    fun getAll(): Flow<Array<ProductCategoryEntity>>

}