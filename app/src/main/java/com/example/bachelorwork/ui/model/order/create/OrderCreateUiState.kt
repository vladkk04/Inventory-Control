package com.example.bachelorwork.ui.model.order.create

import com.example.bachelorwork.domain.model.order.OrderAddedProduct

data class OrderCreateUiState(
    val addedProduct: Set<OrderAddedProduct> = emptySet(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val total: Double = 0.0,
    val comment: String = ""
)

enum class DiscountType {
    PERCENTAGE,
    FIXED
}

