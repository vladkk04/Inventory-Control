package com.example.bachelorwork.data.local.entities.productCategory

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ProductCategoryEntity.TABLE_NAME,
   /* indices = [Index(value = ["name"], unique = true)]*/
)
data class ProductCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int = 0,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    val name: String,
    @DrawableRes
    val icon: Int? = null
) {
    companion object {
        const val TABLE_NAME = "product_categories"
        const val COLUMN_ID = "category_id"
    }
}
