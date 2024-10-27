package com.example.bachelorwork.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productList")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    //@Ignore val image: Uri? = null,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val price: Double,
    //@Ignore val unit: ProductUnit,
    val totalPrice: Double,
   //@Ignore val datePurchase: Date,
    val minStockLevel: Int,
    //@Ignore val category: ProductCategory,
    //@Ignore val tags: List<ProductTag>,
    val description: String,
)