package com.example.inventorycotrol.ui.model.order.manage

import com.example.inventorycotrol.domain.model.order.OrderAddedProduct
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.utils.FileData

data class OrderManageUiState(
    val addedProduct: Set<OrderAddedProduct> = emptySet(),
    val attachments: List<FileData> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val currency: String = "",
    val total: Double = 0.0,
    val comment: String = ""
)