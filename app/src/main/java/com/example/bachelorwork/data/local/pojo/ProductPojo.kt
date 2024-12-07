package com.example.bachelorwork.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.data.local.entities.toProductCategory
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
    pricePerUnit = product.price,
    productUnit = product.productUnit,
    totalPrice = product.totalPrice,
    datePurchase = product.datePurchase,
    minStockLevel = product.minStockLevel,
    description = product.description,
    tags = product.tags,
)
