package com.example.bachelorwork.ui.model.order.manage

import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.utils.FileData

data class OrderManageUiState(
    val addedProduct: Set<OrderAddedProduct> = emptySet(),
    val attachments: List<FileData> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val total: Double = 0.0,
    val comment: String = ""
)