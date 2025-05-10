package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.OrderEntity
import com.example.inventorycotrol.data.local.entities.RawOrderData
import com.example.inventorycotrol.domain.model.order.Order
import com.example.inventorycotrol.ui.model.order.OrderProductUi

fun OrderEntity.mapToDomain() = Order(
    id = id,
    products = emptyList(),
    discount = discount,
    comment = comment,
    attachments = attachments,
    organisationId = organisationId,
    orderedBy = orderedBy,
    orderedAt = orderedAt,
)

fun List<RawOrderData>.mapToDomain(): List<Order> {
    return this.groupBy { it.order.id }
        .map { (orderId, rawOrderDataList) ->
            val firstRaw = rawOrderDataList.first()

            Order(
                id = firstRaw.order.id,
                discount = firstRaw.order.discount,
                comment = firstRaw.order.comment,
                attachments = firstRaw.order.attachments,
                products = rawOrderDataList.map { raw ->
                    OrderProductUi(
                        id = raw.productId,
                        name = raw.productName,
                        imageUrl = raw.imageUrl,
                        unit = raw.unit,
                        quantity = raw.orderQuantity,
                        price = raw.price
                    )
                },
                organisationId = firstRaw.order.organisationId,
                orderedBy = firstRaw.order.orderedBy,
                orderedAt = firstRaw.order.orderedAt
            )
        }
}