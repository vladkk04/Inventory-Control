package com.example.bachelorwork.domain.model.product

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.bachelorwork.ui.model.productCreate.ProductCreateFormState
import com.example.bachelorwork.ui.model.productList.ProductListUI
import java.util.Date

@Entity(
    tableName = Product.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ProductCategory::class,
        parentColumns = [ProductCategory.COLUMN_ID],
        childColumns = [Product.COLUMN_CATEGORY_CHILD_ID],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_CATEGORY_CHILD_ID)
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
    constructor(uiFormState: ProductCreateFormState) : this (
        categoryId = uiFormState.category.id,
        name = uiFormState.name,
        barcode = uiFormState.barcode,
        quantity = uiFormState.quantity,
        price = uiFormState.pricePerUnit.toDouble(),
        productUnit = uiFormState.productUnit,
        totalPrice = uiFormState.totalPrice,
        datePurchase = Date(uiFormState.datePurchase),
        minStockLevel = uiFormState.minStockLevel.toInt(),
        tags = uiFormState.tags,
        description = uiFormState.description
    )

    companion object {
        const val TABLE_NAME = "products"
        const val COLUMN_CATEGORY_CHILD_ID = "categoryId"
    }

    enum class ProductUnit {
        PCS,
        KG,
        CM,
        KM,
        T,
        G,
        BOX;
    }
}

fun List<Product>.toProductListUI(): List<ProductListUI> = map {
    ProductListUI(
        name = it.name,
        price = it.price,
        barcode = it.barcode,
        quantity = it.quantity
    )
}