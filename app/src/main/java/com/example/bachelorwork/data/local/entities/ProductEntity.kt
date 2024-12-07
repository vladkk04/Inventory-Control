package com.example.bachelorwork.data.local.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bachelorwork.data.local.entities.ProductEntity.Companion.CHILD_COLUMN_CATEGORY_ID
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.ui.model.productList.ProductListItemUI
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
    val price: Double,
    val productUnit: ProductUnit,
    val totalPrice: Double,
    val datePurchase: Date,
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

fun ProductEntity.toProduct(productCategory: ProductCategory): Product = Product(
    id = id,
    category = productCategory,
    image = image,
    name = name,
    barcode = barcode,
    quantity = quantity,
    pricePerUnit = price,
    productUnit = productUnit,
    totalPrice = totalPrice,
    datePurchase = datePurchase,
    minStockLevel = minStockLevel,
    tags = tags,
    description = description
)

fun ProductEntity.toProductListItemUI(): ProductListItemUI = ProductListItemUI(
    id = id,
    image = image,
    name = name,
    barcode = barcode,
    price = price,
    quantity = quantity,
    unit = productUnit,
)

