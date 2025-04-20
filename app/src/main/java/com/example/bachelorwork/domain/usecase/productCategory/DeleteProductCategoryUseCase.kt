package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.repository.local.ProductCategoryLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation
import kotlinx.coroutines.flow.Flow

class DeleteProductCategoryUseCase(
    private val remote: ProductCategoryRemoteDataSource,
    private val local: ProductCategoryLocalDataSource
) {
    operator fun invoke(categoryId: String): Flow<Resource<Unit>> = performNetworkOperation(
        remoteCall = { remote.delete(categoryId) },
        localUpdate = { local.deleteById(categoryId) }
    )


    suspend operator fun invoke(vararg productCategory: ProductCategoryEntity) = runCatching {
        local.deleteAll(*productCategory)
    }
}