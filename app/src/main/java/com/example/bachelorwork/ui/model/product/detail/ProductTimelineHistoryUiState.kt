package com.example.bachelorwork.ui.model.product.detail

import com.example.bachelorwork.domain.model.product.ProductTimelineHistory

data class ProductTimelineHistoryUiState(
    val updateHistory: List<ProductTimelineHistory> = emptyList()
)
