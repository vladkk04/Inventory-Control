package com.example.bachelorwork.domain.usecase.order

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.mappers.mapToDomain
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrderRemoteDataSource
import com.example.bachelorwork.ui.model.order.OrderProductUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetOrdersUseCase(
    private val remote: OrderRemoteDataSource,
    private val local: OrderLocalDataSource,
) {
    suspend operator fun invoke(id: String): Flow<Resource<Order>> {
        val rawData = local.getById(id)
        val firstItem = rawData.firstOrNull()

        val order = firstItem?.let {
            Order(
                id = firstItem.order.id,
                discount = firstItem.order.discount,
                comment = firstItem.order.comment,
                attachments = firstItem.order.attachments,
                products = rawData.map { raw ->
                    OrderProductUi(
                        id = raw.productId,
                        imageUrl = raw.imageUrl,
                        name = raw.productName,
                        unit = raw.unit,
                        quantity = raw.orderQuantity,
                        price = raw.price
                    )
                },
                organisationId = firstItem.order.organisationId,
                orderedBy = firstItem.order.orderedBy,
                orderedAt = firstItem.order.orderedAt,
            )
        }

        return remote.get(id).flatMapLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> flowOf(Resource.Loading)
                is ApiResponseResult.Failure -> {
                    flowOf(Resource.Error(errorMessage = response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    order?.let { flowOf(Resource.Success(it)) } ?: flowOf(
                        Resource.Error(
                            errorMessage = "Unknown error"
                        )
                    )
                }
            }.onStart { emit(Resource.Loading) }
                .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<Order>>> {
        return remote.getAll()
            .flatMapLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> flowOf(Resource.Loading)
                    is ApiResponseResult.Failure -> flowOf(Resource.Error(errorMessage = response.errorMessage))
                    is ApiResponseResult.Success -> {
                        local.refresh(orderRemote = response.data)
                        local.getAll().map { it.mapToDomain() }.map { Resource.Success(it) }
                    }
                }
            }
            .onStart { emit(Resource.Loading) }
            .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
    }


}