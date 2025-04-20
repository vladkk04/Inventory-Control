package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.remote.mappers.mapToDomain
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.model.product.ProductRequest
import com.example.bachelorwork.domain.repository.local.ProductLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation

class CreateProductUseCase (
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource
) {
    operator fun invoke(product: ProductRequest) = performNetworkOperation(
        remoteCall = { remote.create(product) },
        localUpdate = { local.insert(it.mapToEntity()) },
        transform = { it.mapToDomain() }
    )
}