package com.example.bachelorwork.domain.usecase.productCategory

import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.model.category.ProductCategoryRequest
import com.example.bachelorwork.domain.repository.local.ProductCategoryLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation
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