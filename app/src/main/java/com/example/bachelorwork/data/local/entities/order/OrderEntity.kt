package com.example.bachelorwork.data.local.entities.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorwork.ui.model.order.DiscountType
import java.util.Date

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = "discount")
    val discount: Double,
    @ColumnInfo(name = "discount_type")
    val discountType: DiscountType,
    @ColumnInfo(name = "total")
    val total: Double,
    @ColumnInfo(name = "ordered_at")
    val orderedAt: Date,
    @ColumnInfo(name = "ordered_by")
    val orderedBy: String,
) {
    companion object {
        const val COLUMN_ID = "order_id"
        const val TABLE_NAME = "orders"
    }
}
