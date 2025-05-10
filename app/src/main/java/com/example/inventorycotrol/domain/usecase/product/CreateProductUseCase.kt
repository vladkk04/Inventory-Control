package com.example.inventorycotrol.domain.usecase.product

import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.product.ProductRequest
import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

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