package com.example.inventorycotrol.data.local.entities


data class ProductUpdateStockDetail(
    val id: String,
    val productsUpdates: List<ProductEntity>,
    val updatedBy: String,
)
