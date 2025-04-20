package com.example.bachelorwork.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProductDetail(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: ProductCategoryEntity,
)
