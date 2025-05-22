package com.example.inventorycotrol.domain.usecase.product

import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class DeleteProductUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {

    operator fun invoke(productId: String) =
        performNetworkOperation(
            remoteCall = { remote.delete(productId) },
            localUpdate = { local.deleteById(productId) }
        )
}