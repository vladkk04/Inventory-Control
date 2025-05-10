package com.example.inventorycotrol.ui.model.order.list

import com.example.inventorycotrol.domain.model.order.OrderProductSubItem
import com.example.inventorycotrol.ui.model.order.DiscountType
import java.util.Date

class OrderUi(
    val id: Int,
    val total: Double,
    val discount: Double,
    val discountType: DiscountType,
    val orderedAt: Date,
    val orderedBy: String,
    val products: List<OrderProductSubItem>
)