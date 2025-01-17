package com.example.bachelorwork.domain.model.order

import android.net.Uri
import com.example.bachelorwork.domain.model.product.ProductUnit

data class OrderAddedProduct(
    val id: Int,
    val name: String,
    val image: Uri? = null,
    val unit: ProductUnit,
    val rate: Double,
    val quantity: Double,
    val total: Double
)

fun OrderAddedProduct.toOrderSubItem() = OrderProductSubItem(
    id = id,
    name = name,
    price = rate,
    quantity = quantity,
    unit = unit.name
)