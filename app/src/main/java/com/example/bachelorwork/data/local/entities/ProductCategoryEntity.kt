package com.example.bachelorwork.data.local.entities

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorwork.domain.model.product.ProductCategory

@Entity(tableName = ProductCategoryEntity.TABLE_NAME)
data class ProductCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int = 0,
    val name: String,
    @DrawableRes
    val icon: Int? = null
) {
    companion object {
        const val TABLE_NAME = "product_categories"
        const val COLUMN_ID = "id"
    }

    enum class DefaultCategories {
        WOOD,
        PLASTIC,
        PAPER,
        METAL
    }
}

fun ProductCategoryEntity.toProductCategory() = ProductCategory(
    id = id,
    name = name,
    icon = icon
)
