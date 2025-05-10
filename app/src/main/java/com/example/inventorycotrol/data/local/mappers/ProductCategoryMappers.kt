package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import com.example.inventorycotrol.domain.model.category.ProductCategory

fun ProductCategoryEntity.mapToDomain() =
    ProductCategory(
        id = this.id,
        name = this.name,
    )
