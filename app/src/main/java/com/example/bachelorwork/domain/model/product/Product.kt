package com.example.bachelorwork.domain.model.product

import android.net.Uri
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.ui.model.productList.ProductListItemUI
import java.util.Date

data class Product(
    var id: Int = 0,
    val category: ProductCategory,
    val image: Uri = Uri.EMPTY,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val productUnit: ProductUnit,
    val totalPrice: Double,
    val datePurchase: Date,
    val minStockLevel: Int,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
)

fun Product.toProductEntity(): ProductEntity = ProductEntity(
    id = id,
    categoryId = category.id,
    image = image,
    name = name,
    barcode = barcode,
    quantity = quantity,
    price = pricePerUnit,
    productUnit = productUnit,
    totalPrice = totalPrice,
    datePurchase = datePurchase,
    minStockLevel = minStockLevel,
    tags = tags,
)

fun List<Product>.toProductListItemUI(): List<ProductListItemUI> = map {
    ProductListItemUI(
        id = it.id,
        name = it.name,
        price = it.pricePerUnit,
        barcode = it.barcode,
        quantity = it.quantity,
        unit = it.productUnit
    )
}