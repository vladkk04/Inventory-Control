package com.example.bachelorwork.ui.model.order.detail

import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.model.order.list.OrderUi

data class OrderDetailUiState(
    val order: OrderUi? = null,
    val discount: Double = 0.0,
    val discountType: DiscountType? = null,
    val subTotal: Double = order?.products?.sumOf { it.price * it.quantity } ?: 0.0,
    val errorMessage: String? = null,
)
