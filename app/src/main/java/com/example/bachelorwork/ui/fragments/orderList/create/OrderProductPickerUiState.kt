package com.example.bachelorwork.ui.fragments.orderList.create

import com.example.bachelorwork.ui.model.OrderSelectableProductUi

data class OrderProductPickerUiState(
    val searchQuery: String = "",
    val products: List<OrderSelectableProductUi> = emptyList(),
)
