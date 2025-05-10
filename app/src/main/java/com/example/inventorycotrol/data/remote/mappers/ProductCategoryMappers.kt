package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.ProductCategoryEntity
import com.example.inventorycotrol.data.remote.dto.ProductCategoryDto
import com.example.inventorycotrol.domain.model.category.ProductCategory

fun ProductCategoryDto.mapToDomain() =
    ProductCategory(
        id = this.id,
        name = this.name,
    )

fun ProductCategoryDto.mapToEntity() =
    ProductCategoryEntity(
        id = this.id,
        name = this.name,
    )

/*
fun ProductCategoryDto.mapToEntity() =
    ProductCategoryEntity(
        id = this.id,
        name = this.name,
    )*/
