package com.example.bachelorwork.domain.usecase.productUpdateStock

data class ProductUpdateStockUseCases(
    val updateStock: UpdateStockUseCase,
    val getStock: GetStockUseCase,
)
