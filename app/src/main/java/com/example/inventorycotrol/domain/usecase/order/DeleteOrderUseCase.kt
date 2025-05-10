package com.example.inventorycotrol.domain.usecase.order

import com.example.inventorycotrol.domain.repository.local.OrderLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class DeleteOrderUseCase(
    private val remote: OrderRemoteDataSource,
    private val local: OrderLocalDataSource
) {
    operator fun invoke(orderId: String) = performNetworkOperation(
        remoteCall = { remote.delete(orderId) },
        localUpdate = { local.deleteById(orderId) }
    )
}