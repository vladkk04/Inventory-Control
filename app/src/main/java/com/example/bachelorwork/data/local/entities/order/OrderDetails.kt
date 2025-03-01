package com.example.bachelorwork.data.local.entities.order

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bachelorwork.data.local.entities.product.ProductEntity

data class OrderDetails (
    @Embedded val order: OrderEntity,

    @Relation(
        parentColumn = OrderEntity.COLUMN_ID,
        entityColumn = ProductEntity.COLUMN_ID,
        associateBy = Junction(OrderProduct::class)
    )
    val products: List<ProductEntity>
)


