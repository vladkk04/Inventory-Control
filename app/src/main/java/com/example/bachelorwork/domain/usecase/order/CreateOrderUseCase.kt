package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.domain.repository.OrderRepository

class CreateOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(product: OrderEntity) = runCatching {
        repository.insert(product)
    }

    suspend operator fun invoke(vararg product: OrderEntity) = runCatching {
        repository.insertAll(*product)
    }
}