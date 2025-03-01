package com.example.bachelorwork.ui.model.order.product

sealed class OrderManageProductFormEvent {
    data class Rate(val rate: String) : OrderManageProductFormEvent()
    data class Quantity(val quantity: String) : OrderManageProductFormEvent()
}