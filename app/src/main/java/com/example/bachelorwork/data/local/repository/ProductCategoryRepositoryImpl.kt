package com.example.bachelorwork.data.local.repository

import com.example.bachelorwork.data.local.dao.ProductCategoryDao
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProductCategoryRepositoryImpl @Inject constructor(
    private val dao: ProductCategoryDao
) : ProductCategoryRepository {

    override suspend fun insert(obj: ProductCategory) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: ProductCategory) = dao.insertAll(*obj)

    override suspend fun update(obj: ProductCategory) = dao.update(obj)

    override suspend fun updateAll(vararg obj: ProductCategory) = dao.updateAll(*obj)

    override suspend fun delete(obj: ProductCategory) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: ProductCategory) = dao.deleteAll(*obj)

    override fun getAll(): Flow<Array<ProductCategory>> = dao.getProductCategories()
}