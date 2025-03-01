package com.example.bachelorwork.data.local.entities.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.bachelorwork.data.local.entities.product.ProductEntity


@Entity(
    tableName = OrderProduct.TABLE_NAME,
    primaryKeys = [OrderProduct.COLUMN_ORDER_ID, OrderProduct.COLUMN_PRODUCT_ID],
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = [OrderEntity.COLUMN_ID],
            childColumns = [OrderProduct.COLUMN_ORDER_ID],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = [ProductEntity.COLUMN_ID],
            childColumns = [OrderProduct.COLUMN_PRODUCT_ID],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(OrderProduct.COLUMN_ORDER_ID), Index(OrderProduct.COLUMN_PRODUCT_ID)]
)
data class OrderProduct(
    @ColumnInfo(name = COLUMN_ORDER_ID)
    val orderId: Int,
    @ColumnInfo(name = COLUMN_PRODUCT_ID)
    val productId: Int,
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    @ColumnInfo(name = "price")
    val price: Double
) {
    companion object {
        const val COLUMN_ORDER_ID = "order_id"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val TABLE_NAME = "order_product_cross_ref"
    }
}