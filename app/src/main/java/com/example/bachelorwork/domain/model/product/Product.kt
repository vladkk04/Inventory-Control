package com.example.bachelorwork.domain.model.product

import android.net.Uri
import com.example.bachelorwork.ui.model.product.list.ProductUI

data class Product(
    var id: Int = 0,
    val category: ProductCategory,
    val image: Uri = Uri.EMPTY,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val unit: ProductUnit,
    val minStockLevel: Int,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
    val timelineHistory: List<ProductTimelineHistory>
)

fun List<Product>.toProductUI(): List<ProductUI> = map {
    ProductUI(
        id = it.id,
        name = it.name,
        barcode = it.barcode,
        quantity = it.quantity,
        unit = it.unit
    )
}