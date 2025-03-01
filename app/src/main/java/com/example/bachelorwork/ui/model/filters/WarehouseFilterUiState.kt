package com.example.bachelorwork.ui.model.filters

import com.example.bachelorwork.domain.model.product.ProductCategory

data class WarehouseFilterUiState(
    val categories: List<ProductCategory> = emptyList(),
)
