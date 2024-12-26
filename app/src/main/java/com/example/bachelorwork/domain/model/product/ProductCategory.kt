package com.example.bachelorwork.domain.model.product

import androidx.annotation.DrawableRes
import com.example.bachelorwork.data.local.entity.ProductCategoryEntity

data class ProductCategory (
    val id: Int = 0,
    val name: String,
    @DrawableRes
    val icon: Int? = null
)

fun ProductCategory.toEntity() = ProductCategoryEntity(
    id = id,
    name = name,
    icon = icon
)