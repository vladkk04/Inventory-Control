package com.example.bachelorwork.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import java.util.Calendar
import java.util.Date

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(
    @PrimaryKey
    val id: Int = ((100000..999999).random().toString() + System.currentTimeMillis()
        .toString()).take(7).toInt(),
    val total: Double,
    val products: List<OrderProductSubItem>,
    val orderedAt: Date = Calendar.getInstance().time,
    val orderedBy: String = "Need to change here later Entity",
) {
    companion object {
        const val TABLE_NAME = "orders"
    }
}

fun OrderEntity.toOrder() = Order(
    id = id,
    total = total,
    items = products,
    orderedAt = orderedAt,
    orderedBy = orderedBy
)
