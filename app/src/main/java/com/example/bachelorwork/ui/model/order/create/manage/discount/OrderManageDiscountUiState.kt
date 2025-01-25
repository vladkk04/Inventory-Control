package com.example.bachelorwork.ui.model.order.create.manage.discount

import com.example.bachelorwork.ui.model.order.create.DiscountType

data class OrderManageDiscountUiState(
    val discount: Double = 0.0,
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val discountError: String? = null
)
