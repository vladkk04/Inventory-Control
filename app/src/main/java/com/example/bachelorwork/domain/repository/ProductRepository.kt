package com.example.bachelorwork.domain.repository

import androidx.paging.PagingData
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface ProductRepository: BaseRoomRepository<ProductEntity> {

    fun getAll(): Flow<PagingData<ProductEntity>>

    fun getById(id: Int): Flow<ProductEntity>
}
