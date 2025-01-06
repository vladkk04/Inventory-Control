package com.example.bachelorwork.ui.model.order.create

sealed class OrderAddProductFormEvent {
    data class Rate(val rate: String) : OrderAddProductFormEvent()
    data class Quantity(val quantity: String) : OrderAddProductFormEvent()
}