package com.example.bachelorwork.ui.model.order.list

import com.example.bachelorwork.domain.model.order.Order

data class OrderListUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
)
