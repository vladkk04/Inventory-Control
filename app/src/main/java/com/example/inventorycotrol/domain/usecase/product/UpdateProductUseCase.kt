package com.example.inventorycotrol.domain.usecase.product

import com.example.inventorycotrol.data.local.mappers.mapToEntity
import com.example.inventorycotrol.data.remote.mappers.mapToRequest
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class UpdateProductUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {
    operator fun invoke(product: Product) = performNetworkOperation(
        remoteCall = { remote.update(product.id, product.mapToRequest()) },
        localUpdate = { local.update(product.mapToEntity()) }
    )

}