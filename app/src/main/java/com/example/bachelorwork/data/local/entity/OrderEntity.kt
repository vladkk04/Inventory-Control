package com.example.bachelorwork.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorwork.domain.model.order.Order
import java.util.Date

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    val total: Double,
    val orderedAt: Date,
    val orderedBy: String,
) {
    companion object {
        const val COLUMN_ID = "orderId"
        const val TABLE_NAME = "orders"
    }
}

fun OrderEntity.toOrder() = Order(
    id = id,
    total = total,
    orderedAt = orderedAt,
    orderedBy = orderedBy
)
