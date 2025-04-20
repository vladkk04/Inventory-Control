package com.example.bachelorwork.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.bachelorwork.domain.model.product.ProductUnit

@Entity("order_product_join",
    primaryKeys = ["order_id", "product_id"],
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["product_id"])
    ]
)
data class OrderProductJoin(
    @ColumnInfo(name = "order_id") val orderId: String,
    @ColumnInfo(name = "product_id") val productId: String,
    val quantity: Double,
    val price: Double
)

data class RawOrderData(
    @Embedded
    val order: OrderEntity,

    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_image_url") val imageUrl: String?,
    @ColumnInfo(name = "product_unit") val unit: ProductUnit,

    @ColumnInfo(name = "quantity") val orderQuantity: Double,
    @ColumnInfo(name = "price") val price: Double
)
