package com.example.bachelorwork.ui.fragments.warehouse.filters

import com.example.bachelorwork.domain.model.category.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag

data class SharedWarehouseFilterUiState(
    val categoryFilters: List<ProductCategory> = emptyList(),
    val stockFilters: List<StockFilter> = emptyList(),
    val tags: List<ProductTag> = emptyList()
)

enum class StockFilter {
    OVERSTOCK, LOW_STOCK, CRITICAL_STOCK, OUT_OF_STOCK
}
