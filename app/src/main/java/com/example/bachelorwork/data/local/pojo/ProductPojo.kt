package com.example.bachelorwork.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bachelorwork.data.local.entity.ProductCategoryEntity
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.data.local.entity.toProductCategory
import com.example.bachelorwork.domain.model.product.Product

data class ProductPojo(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = ProductEntity.CHILD_COLUMN_CATEGORY_ID,
        entityColumn = ProductCategoryEntity.COLUMN_ID
    )
    val category: ProductCategoryEntity
)

fun ProductPojo.toProduct() = Product(
    id = product.id,
    category = category.toProductCategory(),
    image = product.image,
    name = product.name,
    barcode = product.barcode,
    quantity = product.quantity,
    unit = product.productUnit,
    minStockLevel = product.minStockLevel,
    description = product.description,
    tags = product.tags,
    timelineHistory = product.timelineHistory
)
