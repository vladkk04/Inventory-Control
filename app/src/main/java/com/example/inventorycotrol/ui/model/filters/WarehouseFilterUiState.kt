package com.example.inventorycotrol.ui.model.filters

import com.example.inventorycotrol.domain.model.category.ProductCategory

data class WarehouseFilterUiState(
    val categories: List<ProductCategory> = emptyList(),
)


