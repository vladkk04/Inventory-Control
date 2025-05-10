package com.example.inventorycotrol.ui.model.product.detail

import com.example.inventorycotrol.domain.model.product.ProductTimelineHistory

data class ProductTimelineHistoryUiState(
    val updateHistory: List<ProductTimelineHistory> = emptyList()
)
