package com.example.bachelorwork.ui.model.order

import android.net.Uri
import com.example.bachelorwork.domain.model.product.ProductUnit

data class OrderSelectableProductListUi(
    val id: Int,
    val image: Uri? = null,
    val name: String,
    val unit: ProductUnit,
    val isSelected: Boolean = false
)
