package com.example.bachelorwork.ui.model.order

import com.example.bachelorwork.domain.model.order.Order

data class OrderDetailUiState(
    val order: Order? = null,
    val errorMessage: String? = null,
)
