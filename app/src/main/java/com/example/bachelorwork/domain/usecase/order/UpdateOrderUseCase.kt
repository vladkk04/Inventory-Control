package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrderRemoteDataSource

class UpdateOrderUseCase(
    private val remote: OrderRemoteDataSource,
    private val repository: OrderLocalDataSource
) {
    suspend operator fun invoke(order: OrderEntity) = runCatching {
        repository.update(order)
    }

    suspend operator fun invoke(vararg orders: OrderEntity) = runCatching {
        repository.updateAll(*orders)
    }
}