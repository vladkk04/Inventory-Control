package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface ProductRepository: BaseRepository<Product> {
    fun getProductById(id: Int): Flow<Product>
}