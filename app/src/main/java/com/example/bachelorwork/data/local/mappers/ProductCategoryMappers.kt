package com.example.bachelorwork.data.local.mappers

import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.domain.model.category.ProductCategory

fun ProductCategoryEntity.mapToDomain() =
    ProductCategory(
        id = this.id,
        name = this.name,
    )
