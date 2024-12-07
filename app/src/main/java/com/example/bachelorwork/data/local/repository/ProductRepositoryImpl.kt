package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.data.local.pojo.ProductPojo
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productDao: ProductDao,
): ProductRepository {

    override suspend fun insert(obj: ProductEntity) = productDao.insert(obj)

    override suspend fun insertAll(vararg obj: ProductEntity) = productDao.insertAll(*obj)

    override suspend fun update(obj: ProductEntity) = productDao.update(obj)

    override suspend fun updateAll(vararg obj: ProductEntity) = productDao.updateAll(*obj)

    override suspend fun deleteByProductId(id: Int) = productDao.deleteByProductId(id)

    override suspend fun delete(obj: ProductEntity) = productDao.delete(obj)

    override suspend fun deleteAll(vararg obj: ProductEntity) = productDao.deleteAll(*obj)

    override fun getProductsPojo(): Flow<List<ProductPojo>> = productDao.getProductsPojo()

    override fun getProductPojoById(id: Int): Flow<ProductPojo> = productDao.getProductPojo(id)

}