package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val dao: ProductDao
): ProductRepository {

    override suspend fun insert(obj: Product) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: Product) = dao.insertAll(*obj)

    override suspend fun update(obj: Product) = dao.update(obj)

    override suspend fun updateAll(vararg obj: Product) = dao.updateAll(*obj)

    override suspend fun delete(obj: Product) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: Product) = dao.deleteAll(*obj)

    override fun getAll(): Flow<Array<Product>> = dao.getProducts()

    override fun getProductById(id: Int): Flow<Product> = dao.getProductById(id)

}