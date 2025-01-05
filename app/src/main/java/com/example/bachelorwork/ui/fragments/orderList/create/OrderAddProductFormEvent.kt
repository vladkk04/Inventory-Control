package com.example.bachelorwork.ui.fragments.orderList.create

sealed class OrderAddProductFormEvent {
    data class Rate(val rate: String) : OrderAddProductFormEvent()
    data class Quantity(val quantity: String) : OrderAddProductFormEvent()
}