package com.example.inventorycotrol.domain.usecase.productCategory

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import com.example.inventorycotrol.domain.model.category.ProductCategoryRequest
import com.example.inventorycotrol.domain.repository.local.ProductCategoryLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation
import kotlinx.coroutines.flow.Flow

class UpdateProductCategoryUseCase(
    private val remote: ProductCategoryRemoteDataSource,
    private val local: ProductCategoryLocalDataSource
) {
    operator fun invoke(categoryId: String, request: ProductCategoryRequest): Flow<Resource<Unit>> = performNetworkOperation(
        remoteCall = { remote.update(categoryId, request) },
        localUpdate = {
            val entity = ProductCategoryEntity(
                id = categoryId,
                name = request.name
            )
            local.update(entity)
        }
    )
}