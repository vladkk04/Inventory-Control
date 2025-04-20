package com.example.bachelorwork.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val products: List<OrderProduct>,
    val discount: OrderDiscount,
    val comment: String? = null,
    val attachments: List<Attachment>,
)
