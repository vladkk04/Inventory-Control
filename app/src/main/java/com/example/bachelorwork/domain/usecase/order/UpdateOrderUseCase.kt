package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.domain.repository.OrderRepository

class UpdateOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(order: OrderEntity) = runCatching {
        repository.update(order)
    }

    suspend operator fun invoke(vararg orders: OrderEntity) = runCatching {
        repository.updateAll(*orders)
    }
}