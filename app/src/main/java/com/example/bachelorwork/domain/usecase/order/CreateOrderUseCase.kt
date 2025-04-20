package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entities.OrderProductJoin
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.model.order.OrderRequest
import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrderRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation

class CreateOrderUseCase(
    private val remote: OrderRemoteDataSource,
    private val local: OrderLocalDataSource
) {
    operator fun invoke(request: OrderRequest) = performNetworkOperation(
        remoteCall = {
            remote.create(request)
        },
        localUpdate = { order ->
            local.insert(order.mapToEntity())

            order.products.forEach { product ->
                local.insertOrderDetail(
                    OrderProductJoin(
                        orderId = order.id,
                        productId = product.productId,
                        quantity = product.quantity,
                        price = product.price
                    )
                )
            }
        }
    )
}