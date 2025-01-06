package com.example.bachelorwork.ui.model.order

data class OrderSelectableProductUi(
    val id: Int,
    val image: String? = null,
    val name: String,
    val isSelected: Boolean = false
)
