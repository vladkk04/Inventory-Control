package com.example.inventorycotrol.domain.usecase.productUpdateStock

data class ProductUpdateStockUseCases(
    val updateStock: UpdateStockUseCase,
    val getStock: GetStockUseCase,
)
