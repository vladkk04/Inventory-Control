package com.example.bachelorwork.data.remote.mappers

import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.data.remote.dto.OrderDto
import com.example.bachelorwork.domain.model.order.Order


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
