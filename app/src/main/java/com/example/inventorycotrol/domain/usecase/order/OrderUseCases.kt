package com.example.inventorycotrol.domain.usecase.order

data class OrderUseCases(
    val createOrder: CreateOrderUseCase,
    val updateOrder: UpdateOrderUseCase,
    val deleteOrder: DeleteOrderUseCase,
    val getOrders: GetOrdersUseCase,
)
