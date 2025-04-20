package com.example.bachelorwork.ui.model.filters

import com.example.bachelorwork.domain.model.category.ProductCategory

data class WarehouseFilterUiState(
    val categories: List<ProductCategory> = emptyList(),
)


