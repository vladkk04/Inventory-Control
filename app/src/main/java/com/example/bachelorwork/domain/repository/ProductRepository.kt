package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.data.local.pojo.ProductPojo
import com.example.bachelorwork.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface ProductRepository: BaseRepository<ProductEntity> {

    fun getProductsPojo(): Flow<List<ProductPojo>>

    fun getProductPojoById(id: Int): Flow<ProductPojo>

    suspend fun deleteByProductId(id: Int)
}
