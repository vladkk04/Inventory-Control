package com.example.bachelorwork.domain.usecase.order

data class OrderUseCases(
    val createOrder: CreateOrderUseCase,
    val updateOrder: UpdateOrderUseCase,
    val getOrders: GetOrdersUseCase,
)
