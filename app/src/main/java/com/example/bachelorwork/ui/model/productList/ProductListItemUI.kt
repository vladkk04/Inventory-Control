package com.example.bachelorwork.ui.model.productList

import android.net.Uri
import com.example.bachelorwork.domain.model.product.ProductUnit

data class ProductListItemUI(
    val id: Int,
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val price: Double,
    val unit: ProductUnit,
    val quantity: Int
)