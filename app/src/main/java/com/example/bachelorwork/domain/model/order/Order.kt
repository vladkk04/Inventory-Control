package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.model.order.OrderProductUi
import kotlin.math.max

data class Order(
    val id: String,
    val products: List<OrderProductUi>,
    val discount: OrderDiscount,
    val comment: String? = null,
    val attachments: List<Attachment>,
    val organisationId: String,
    val orderedBy: String,
    val orderedAt: Long
) {
    val subTotal = products.sumOf { it.price * it.quantity }
    val total = calculateOrderTotal(this)

    private fun calculateOrderTotal(order: Order): Double {
        val subtotal = order.products.sumOf { it.price * it.quantity }

        return when (order.discount.type) {
            DiscountType.PERCENTAGE -> {
                subtotal * (1 - order.discount.value / 100)
            }
            DiscountType.FIXED -> {
                max(0.0, subtotal - order.discount.value)
            }
        }
    }
}



