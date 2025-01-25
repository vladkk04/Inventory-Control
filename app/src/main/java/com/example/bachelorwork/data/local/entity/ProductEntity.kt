package com.example.bachelorwork.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bachelorwork.data.local.entity.ProductEntity.Companion.CHILD_COLUMN_CATEGORY_ID
import com.example.bachelorwork.data.local.entity.ProductEntity.Companion.COLUMN_NAME
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.domain.model.product.ProductUnit

@Entity(
    tableName = ProductEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ProductCategoryEntity::class,
            parentColumns = [ProductCategoryEntity.COLUMN_ID],
            childColumns = [CHILD_COLUMN_CATEGORY_ID],
            onDelete = ForeignKey.SET_DEFAULT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(CHILD_COLUMN_CATEGORY_ID),
        Index(value = [COLUMN_NAME], unique = true)
    ],
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Int = 0,
    @ColumnInfo(name = CHILD_COLUMN_CATEGORY_ID, defaultValue = "NULL")
    val categoryId: Int?,
    val image: Uri = Uri.EMPTY,
    @ColumnInfo(name = COLUMN_NAME, collate = ColumnInfo.NOCASE)
    val name: String,
    val barcode: String,
    val quantity: Int,
    val productUnit: ProductUnit,
    val minStockLevel: Int,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
    val timelineHistory: List<ProductTimelineHistory>,
) {
    companion object {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = "productId"
        const val CHILD_COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_NAME = "name"
    }
}

