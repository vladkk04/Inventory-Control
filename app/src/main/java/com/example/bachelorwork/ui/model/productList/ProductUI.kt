package com.example.bachelorwork.ui.model.productList

import android.net.Uri
import com.example.bachelorwork.domain.model.product.ProductUnit

data class ProductUI(
    val id: Int,
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val unit: ProductUnit,
    val quantity: Int
)