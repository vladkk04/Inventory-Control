package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.domain.model.product.ProductDetailOrderItem
import java.util.Date

data class Order(
    val id: Int,
    val items: List<OrderProductSubItem>,
    val total: Double,
    val orderedAt: Date,
    val orderedBy: String,
)

fun Order.toOrderEntity(): OrderEntity = OrderEntity(
    id = this.id,
    products = this.items,
    total = this.total,
    orderedAt = this.orderedAt,
    orderedBy = this.orderedBy
)


fun Order.toProductDetailOrderItem(productId: Int): ProductDetailOrderItem {
    return ProductDetailOrderItem(
        orderId = this.id,
        orderedAt = this.orderedAt,
        orderProductSubItem = items.first { it.id == productId }
    )
}


