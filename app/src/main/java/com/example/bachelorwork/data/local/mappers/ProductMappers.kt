package com.example.bachelorwork.data.local.mappers

import com.example.bachelorwork.data.local.entities.product.ProductDetails
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory

fun ProductCategoryEntity.mapToDomain() = ProductCategory(
    id = id,
    name = name,
    icon = icon
)

fun ProductCategory.mapToEntity() = ProductCategoryEntity(
    id = id,
    name = name,
    icon = icon
)

fun Product.mapToEntity() = ProductEntity(
    id = id,
    categoryId = category?.id,
    image = image,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    minStockLevel = minStockLevel,
    description = description,
    tags = tags,
    timelineHistory = timelineHistory
)


fun ProductDetails.mapToDomain() = Product(
    id = this.product.id,
    category = this.category.mapToDomain(),
    image = this.product.image,
    name = this.product.name,
    barcode = this.product.barcode,
    quantity = this.product.quantity,
    unit = this.product.unit,
    minStockLevel = this.product.minStockLevel,
    description = this.product.description,
    tags = this.product.tags,
    timelineHistory = this.product.timelineHistory,
)

fun ProductEntity.mapToDomain() = Product(
    id = this.id,
    category = null,
    image = this.image,
    name = this.name,
    barcode = this.barcode,
    quantity = this.quantity,
    unit = this.unit,
    minStockLevel = this.minStockLevel,
    description = this.description,
    tags = this.tags,
    timelineHistory = this.timelineHistory,
)
