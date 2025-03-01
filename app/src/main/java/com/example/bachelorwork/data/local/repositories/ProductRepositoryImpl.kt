package com.example.bachelorwork.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val dao: ProductDao,
): ProductRepository {

    override fun getAll(): Flow<PagingData<ProductEntity>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { dao.getAll() }
    ).flow

    override fun getById(id: Int): Flow<ProductEntity> = dao.getById(id)

    override suspend fun insert(obj: ProductEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: ProductEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: ProductEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: ProductEntity) = dao.updateAll(*obj)

    override suspend fun delete(obj: ProductEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: ProductEntity) = dao.deleteAll(*obj)

}