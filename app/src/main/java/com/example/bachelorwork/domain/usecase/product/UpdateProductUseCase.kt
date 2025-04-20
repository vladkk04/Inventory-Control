package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.mappers.mapToEntity
import com.example.bachelorwork.data.remote.mappers.mapToRequest
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.local.ProductLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation

class UpdateProductUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {

    operator fun invoke(product: Product) = performNetworkOperation(
        remoteCall = { remote.update(product.id, product.mapToRequest()) },
        localUpdate = { local.update(product.mapToEntity()) }
    )

}