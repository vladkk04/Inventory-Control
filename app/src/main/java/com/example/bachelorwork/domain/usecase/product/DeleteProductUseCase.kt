package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.repository.local.ProductLocalDataSource
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation

class DeleteProductUseCase(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource,
) {

    operator fun invoke(productId: String) =
        performNetworkOperation(remoteCall = { remote.delete(productId) },
            localUpdate = { local.deleteById(productId) })

}