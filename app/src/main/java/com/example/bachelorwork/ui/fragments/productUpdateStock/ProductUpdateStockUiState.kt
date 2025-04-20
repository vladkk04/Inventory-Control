package com.example.bachelorwork.ui.fragments.productUpdateStock

import com.example.bachelorwork.ui.model.order.SelectableProductUi
import com.example.bachelorwork.ui.model.productUpdateStock.StockAdjustmentUi


data class ProductUpdateStockUiState(
    val products: List<SelectableProductUi> = emptyList(),
    val adjustments: List<StockAdjustmentUi> = emptyList(),
    val canAddAdjustment: Boolean = false,
    val isLoading: Boolean = true
)