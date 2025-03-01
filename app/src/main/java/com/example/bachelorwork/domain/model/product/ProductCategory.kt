package com.example.bachelorwork.domain.model.product

import androidx.annotation.DrawableRes
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity

data class ProductCategory (
    val id: Int = 0,
    val name: String,
    @DrawableRes
    val icon: Int? = null
)

fun ProductCategory.toEntity() = ProductCategoryEntity(
    id = id,
    name = name.replaceFirstChar(Char::uppercase),
    icon = icon
)