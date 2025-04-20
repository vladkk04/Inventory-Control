package com.example.bachelorwork.data.local.entities

import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType

data class ProductUpdateStockDetail(
    val id: String,
    val productsUpdates: List<ProductEntity>,
    val stockOperationType: StockOperationType,
    val updatedBy: String,
)
