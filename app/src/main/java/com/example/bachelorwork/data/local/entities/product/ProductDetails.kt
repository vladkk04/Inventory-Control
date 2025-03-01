package com.example.bachelorwork.data.local.entities.product

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.data.local.entities.order.OrderProduct
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity

data class ProductDetails(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = ProductEntity.CHILD_COLUMN_CATEGORY_ID,
        entityColumn = ProductCategoryEntity.COLUMN_ID
    )
    val category: ProductCategoryEntity,
    @Relation(
        parentColumn =  ProductEntity.COLUMN_ID,
        entityColumn = OrderEntity.COLUMN_ID,
        associateBy = Junction(OrderProduct::class)
    )
    val orders: List<OrderEntity>
)
