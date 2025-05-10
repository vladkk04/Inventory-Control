package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.ProductEntity
import com.example.inventorycotrol.data.remote.dto.ProductDto
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.model.product.ProductRequest
import com.example.inventorycotrol.ui.model.order.SelectableProductUi
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockItem

fun ProductDto.mapToDomain(): Product = Product(
    id = id,
    imageUrl = imageUrl,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    categoryName = categoryId,
    minStockLevel = minStockLevel,
    description = description,
    organisationId = organisationId,
    createdBy = createdBy,
    createdAt = createdAt,
    tags = tags,
    updates = updates
)

fun ProductDto.mapToEntity() = ProductEntity(
    id = id,
    imageUrl = imageUrl,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    categoryId = categoryId,
    minStockLevel = minStockLevel,
    description = description,
    organisationId = organisationId,
    createdBy = createdBy,
    createdAt = createdAt,
    tags = tags,
    updates = updates
)

fun Product.mapToSelection() = SelectableProductUi(
    id = id,
    image = this.imageUrl,
    name = name,
    unit = unit,
    currentStock = this.quantity,
    minStockLevel = minStockLevel,
    barcode = barcode,
    isSelected = false
)

fun Product.mapToUpdateStock() = ProductUpdateStockItem(
    id = id,
    name = name,
    unit = unit,
    minStockLevel = minStockLevel,
    stockOnHand = this.quantity,
)

fun Product.mapToRequest() = ProductRequest(
    imageUrl = imageUrl,
    name = name,
    barcode = barcode,
    quantity = quantity,
    unit = unit,
    categoryId = categoryName ?: "",
    minStockLevel = minStockLevel,
    description = description,
    tags = tags,
)
