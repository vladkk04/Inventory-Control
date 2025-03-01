package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetOrdersUseCase(
    private val repository: OrderRepository,
) {
    operator fun invoke(): Flow<Result<List<Order>>> = emptyFlow()
        /*repository.getOrdersPojo().map {
            runCatching { it.map { it.toOrder() } }
        }*/

    operator fun invoke(id: Int): Flow<Result<Order>> = emptyFlow()
        /*repository.getOrderPojo(id)
            .map { product ->
                product.runCatching {
                    val filteredProducts = this.products.filter { productCrossRef ->
                        productCrossRef.orderId == id
                    }
                    this.copy(products = filteredProducts).toOrder()
                }
            }*/
}