package com.example.bachelorwork.data.remote.mappers

import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.data.remote.dto.ProductCategoryDto
import com.example.bachelorwork.domain.model.category.ProductCategory

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
