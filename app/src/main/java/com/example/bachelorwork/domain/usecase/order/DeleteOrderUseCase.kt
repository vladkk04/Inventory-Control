package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrderRemoteDataSource

class DeleteOrderUseCase(
    private val remote: OrderRemoteDataSource,
    private val repository: OrderLocalDataSource
) {
    suspend operator fun invoke(orderId: String) = remote.delete(orderId)

    /*suspend operator fun getAll(id: Int) = runCatching {
        repository.delete(id)
    }*/

}