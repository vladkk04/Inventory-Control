package com.example.bachelorwork.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorwork.domain.model.order.Order
import java.util.Date

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(
    @PrimaryKey
    val id: Int = 0,
    val date: Date,
    val total: Double
    //val items: List<OrderSubItem>
) {
    companion object {
        const val TABLE_NAME = "orders"
    }
}

fun OrderEntity.toOrder() = Order(
    id = id.toString(),
    date = date.toString(),
    total = total,
    items = emptyList()
)