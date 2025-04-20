package com.example.bachelorwork.ui.model.order.product

sealed class OrderManageProductFormEvent {
    data class Search(val query: String) : OrderManageProductFormEvent()
    data class Price(val price: String) : OrderManageProductFormEvent()
    data class Quantity(val quantity: String) : OrderManageProductFormEvent()
}