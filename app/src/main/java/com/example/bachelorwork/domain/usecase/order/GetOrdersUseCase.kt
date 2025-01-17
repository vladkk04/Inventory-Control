package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.data.local.entity.toOrder
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOrdersUseCase(
    private val repository: OrderRepository
) {
    operator fun invoke(): Flow<Result<List<Order>>> {
        return repository.getAll().map { orderEntity ->
            val toProducts = orderEntity.map { it.toOrder() }
            runCatching { toProducts }
        }
    }
}