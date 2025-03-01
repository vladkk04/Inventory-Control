package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.domain.repository.OrderRepository

class CreateOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(order: OrderEntity, vararg addedProducts: OrderAddedProduct) =
        runCatching {
            /*repository.insertAndAddProductsInTransaction(
                order, addedProducts = addedProducts
            )*/
        }

    suspend operator fun invoke(vararg orders: OrderEntity) = runCatching {
        repository.insertAll(*orders)
    }
}