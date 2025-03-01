package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.domain.repository.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderEntity: OrderEntity) = runCatching {
        repository.delete(orderEntity)
    }

    /*suspend operator fun invoke(id: Int) = runCatching {
        repository.delete(id)
    }*/

}