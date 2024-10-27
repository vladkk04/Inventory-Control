package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProducts(): Flow<List<Product>>

    suspend fun getProductById(id: Int): Product?

    suspend fun insertProduct(product: Product)

    suspend fun deleteProduct(product: Product)
}