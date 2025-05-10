package com.example.inventorycotrol.domain.usecase.order

import com.example.inventorycotrol.data.local.entities.OrderProductJoin
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.order.OrderRequest
import com.example.inventorycotrol.domain.repository.local.OrderLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

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
        },
        transform = { it }
    )
}