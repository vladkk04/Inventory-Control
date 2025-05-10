package com.example.inventorycotrol.domain.usecase.productCategory

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import com.example.inventorycotrol.domain.repository.local.ProductCategoryLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation
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