package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.ProductEntity
import com.example.inventorycotrol.domain.model.product.Product

fun ProductEntity.mapToDomain() = Product(
    id = id,
    categoryName = categoryId,
    imageUrl = imageUrl,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    minStockLevel = minStockLevel,
    description = description,
    tags = tags,
    updates = updates,
    organisationId = organisationId,
    createdBy = createdBy,
    createdAt = createdAt
)

fun Product.mapToEntity() = ProductEntity(
    id = id,
    categoryId = null,
    imageUrl = imageUrl,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    minStockLevel = minStockLevel,
    description = description,
    tags = tags,
    updates = updates,
    organisationId = organisationId,
    createdBy = createdBy,
    createdAt = createdAt
)
