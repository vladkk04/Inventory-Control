package com.example.inventorycotrol.domain.usecase.order

import com.example.inventorycotrol.data.local.entities.OrderEntity
import com.example.inventorycotrol.domain.repository.local.OrderLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource

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