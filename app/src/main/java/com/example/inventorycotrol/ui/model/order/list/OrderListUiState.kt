package com.example.inventorycotrol.ui.model.order.list

import com.example.inventorycotrol.domain.model.order.Order

data class OrderListUiState(
    val orders: List<Order> = emptyList(),
    val imageUrl: String? = null,
    val currency: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
