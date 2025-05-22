package com.example.inventorycotrol.domain.usecase.order

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.OrderProductJoin
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.domain.model.order.Order
import com.example.inventorycotrol.domain.repository.local.OrderLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource
import com.example.inventorycotrol.ui.model.order.OrderProductUi
import com.example.inventorycotrol.ui.utils.extensions.flattingRemoteToLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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

        if (order == null) {
            return flowOf(Resource.Error(errorMessage = "Unknown error"))
        }

        return remote.get(id).flattingRemoteToLocal(
            onFailureBlock = { errorMessage ->
                flowOf(Resource.Error(order, errorMessage = errorMessage))
            },
            onSuccessBlock = { _ ->
                flowOf(Resource.Success(order))
            }
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<Order>>> {
        return remote.getAll().flattingRemoteToLocal(
            onFailureBlock = { errorMessage ->
                local.getAll().map { it.mapToDomain() }.map { Resource.Error(it, errorMessage) }
            },
            onSuccessBlock = { response ->
                local.refresh(orderRemote = response)
                val result =
                    response.flatMapIndexed { _: Int, orderDto: OrderDto ->
                        orderDto.products.map { product ->
                            OrderProductJoin(
                                orderId = orderDto.id,
                                productId = product.productId,
                                quantity = product.quantity,
                                price = product.price
                            )
                        }
                    }
                local.upsertOrderJoin(*result.toTypedArray())
                local.getAll().map { it.mapToDomain() }.map { Resource.Success(it) }
            }
        )
    }


}