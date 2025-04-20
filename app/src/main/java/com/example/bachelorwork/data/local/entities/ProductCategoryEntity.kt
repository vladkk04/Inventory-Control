package com.example.bachelorwork.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ProductCategoryEntity.TABLE_NAME
)
data class ProductCategoryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "product_categories"
    }
}
