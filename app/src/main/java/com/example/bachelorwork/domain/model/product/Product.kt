package com.example.bachelorwork.domain.model.product

import android.net.Uri
import com.example.bachelorwork.ui.model.product.list.ProductUi

data class Product(
    var id: Int = 0,
    val category: ProductCategory?,
    val image: Uri = Uri.EMPTY,
    val name: String,
    val barcode: String,
    val quantity: Double,
    val unit: ProductUnit,
    val minStockLevel: Double,
    val tags: List<ProductTag> = emptyList(),
    val description: String = "",
    val timelineHistory: List<ProductTimelineHistory>
)

fun Product.toProductUi() = ProductUi(
    id = id,
    image = image,
    name = name,
    barcode = barcode,
    unit = unit,
    category = category?.name,
    minStockLevel = minStockLevel,
    quantity = quantity
)