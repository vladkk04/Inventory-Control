package com.example.bachelorwork.data.local.entities.product

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.bachelorwork.data.local.entities.product.ProductEntity.Companion.CHILD_COLUMN_CATEGORY_ID
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity
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
   /* indices = [
        Index(CHILD_COLUMN_CATEGORY_ID),
        Index(value = ["name"], unique = true)
    ],*/
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Int = 0,
    @ColumnInfo(name = CHILD_COLUMN_CATEGORY_ID, defaultValue = "NULL")
    val categoryId: Int? = null,
    @ColumnInfo(name = "image")
    val image: Uri = Uri.EMPTY,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    val name: String,
    @ColumnInfo(name = "barcode")
    val barcode: String,
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    @ColumnInfo(name = "unit")
    val unit: ProductUnit,
    @ColumnInfo(name = "min_stock_level")
    val minStockLevel: Double,
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "tags")
    val tags: List<ProductTag> = emptyList(),
    @ColumnInfo(name = "timeline_history")
    val timelineHistory: List<ProductTimelineHistory>,
) {
    companion object {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = "product_id"
        const val CHILD_COLUMN_CATEGORY_ID = "category_id"
    }
}


