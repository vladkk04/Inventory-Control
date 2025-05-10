package com.example.inventorycotrol.ui.model.order.detail

import com.example.inventorycotrol.domain.model.order.Order

data class OrderDetailUiState(
    val order: Order? = null,
    val currency: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
