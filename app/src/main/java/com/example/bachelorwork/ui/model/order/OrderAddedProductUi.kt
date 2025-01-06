package com.example.bachelorwork.ui.model.order

import android.net.Uri
import com.example.bachelorwork.domain.model.product.ProductUnit

data class OrderAddedProductUi(
    val name: String,
    val image: Uri? = null,
    val unit: ProductUnit,
    val quantity: Int,
    val rate: Int,
    val total: Int = quantity * rate
)
