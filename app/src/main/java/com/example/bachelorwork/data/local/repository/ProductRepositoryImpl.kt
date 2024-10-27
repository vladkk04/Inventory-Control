package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.ProductDao
import com.example.bachelorwork.domain.model.Product
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val dao: ProductDao
): ProductRepository {
    override fun getProducts(): Flow<List<Product>> {
        return dao.getProducts()
    }

    override suspend fun getProductById(id: Int): Product? {
        return dao.getProductById(id)
    }

    override suspend fun insertProduct(product: Product) {
        return dao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        return dao.deleteProduct(product)
    }
}