package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.dao.ProductCategoryDao
import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProductCategoryRepositoryImpl @Inject constructor(
    private val dao: ProductCategoryDao
) : ProductCategoryRepository {

    override suspend fun insert(obj: ProductCategoryEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: ProductCategoryEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: ProductCategoryEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: ProductCategoryEntity) = dao.updateAll(*obj)

    override suspend fun delete(obj: ProductCategoryEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: ProductCategoryEntity) = dao.deleteAll(*obj)

    override fun getAll(): Flow<Array<ProductCategoryEntity>> = dao.getProductCategories()

}
