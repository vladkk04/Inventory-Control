package com.example.inventorycotrol.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventorycotrol.domain.model.product.ProductTag
import com.example.inventorycotrol.domain.model.product.ProductUnit
import com.example.inventorycotrol.domain.model.product.ProductUpdateHistory
import kotlinx.serialization.SerialName

@Entity(
    tableName = ProductEntity.TABLE_NAME
)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("image_url")
    val imageUrl: String? = null,
    val name: String,
    val barcode: String,
    val quantity: Double,
    val unit: ProductUnit,
    @ColumnInfo("category_id")
    val categoryId: String? = null,
    @ColumnInfo("min_stock_level")
    val minStockLevel: Double,
    val description: String? = null,
    @ColumnInfo("organisation_id")
    val organisationId: String,
    val tags: List<ProductTag>,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("created_at")
    val createdAt: Long,
    val updates: List<ProductUpdateHistory>
) {
    companion object {
        const val TABLE_NAME = "products"
    }
}


