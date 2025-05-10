package com.example.inventorycotrol.ui.model.productUpdateStock

data class ProductUpdateStockCompact(
    val id: String,
    val totalUpdate: Int,
    val updateAt: Long,
    val updateBy: String
)
