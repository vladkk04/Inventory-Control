package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.OrderEntity
import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.domain.model.order.Order


fun OrderDto.mapToDomain() = Order(
    id = this.id,
    products = emptyList(),
    discount = this.discount,
    comment = this.comment,
    attachments = this.attachments,
    organisationId = this.organisationId,
    orderedBy = this.orderedBy,
    orderedAt = this.orderedAt
)

fun OrderDto.mapToEntity() = OrderEntity(
    id = this.id,
    discount = this.discount,
    comment = this.comment,
    attachments = this.attachments,
    organisationId = this.organisationId,
    orderedBy = this.orderedBy,
    orderedAt = this.orderedAt,
)
