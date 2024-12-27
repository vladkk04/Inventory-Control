package com.example.bachelorwork.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bachelorwork.data.local.entity.ProductEntity.Companion.CHILD_COLUMN_CATEGORY_ID
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit
import java.util.Date


@Entity(
    tableName = ProductEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ProductCategoryEntity::class,
        parentColumns = [ProductCategoryEntity.COLUMN_ID],
        childColumns = [CHILD_COLUMN_CATEGORY_ID],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(CHILD_COLUMN_CATEGORY_ID)],
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Int = 0,
    @ColumnInfo(name = CHILD_COLUMN_CATEGORY_ID)
    val categoryId: Int,
    val image: Uri = Uri.EMPTY,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val productUnit: ProductUnit,
    val minStockLevel: Int,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
) {
    companion object {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = "id"
        const val CHILD_COLUMN_CATEGORY_ID = "categoryId"
    }
}

