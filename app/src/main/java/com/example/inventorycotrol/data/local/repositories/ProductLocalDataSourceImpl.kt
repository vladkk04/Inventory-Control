package com.example.inventorycotrol.data.local.repositories

import com.example.inventorycotrol.data.local.dao.ProductDao
import com.example.inventorycotrol.data.local.entities.ProductDetail
import com.example.inventorycotrol.data.local.entities.ProductEntity
import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSourceImpl(
    private val dao: ProductDao,
): ProductLocalDataSource {

    override fun getAll(): Flow<List<ProductEntity>> = dao.getAll()

    override fun getById(id: String): Flow<ProductEntity> = dao.getById(id)

    override fun getByIdWithCategory(id: String): Flow<ProductDetail> = dao.getByIdWithCategory(id)

    override fun getAllWithCategory(): Flow<List<ProductDetail>> = dao.getProductsWithCategory()

    override suspend fun refresh(productRemote: List<ProductEntity>) = dao.refresh(productRemote)

    override suspend fun deleteById(id: String)  = dao.deleteById(id)

    override suspend fun insert(obj: ProductEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: ProductEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: ProductEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: ProductEntity) = dao.updateAll(*obj)

    override suspend fun upsert(obj: ProductEntity) = dao.upsert(obj)

    override suspend fun upsertAll(vararg obj: ProductEntity) = dao.upsertAll(*obj)

    override suspend fun delete(obj: ProductEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: ProductEntity) = dao.deleteAll(*obj)


}