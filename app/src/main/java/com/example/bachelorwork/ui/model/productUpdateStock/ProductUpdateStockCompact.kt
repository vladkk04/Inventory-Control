package com.example.bachelorwork.ui.model.productUpdateStock

import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType

data class ProductUpdateStockCompact(
    val id: String,
    val totalUpdate: Int,
    val operationType: StockOperationType,
    val updateAt: Long,
    val updateBy: String
)
