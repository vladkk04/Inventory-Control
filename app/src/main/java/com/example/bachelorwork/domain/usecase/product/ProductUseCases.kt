package com.example.bachelorwork.domain.usecase.product

data class ProductUseCases(
    val createProduct: CreateProductUseCase,
    val deleteProduct: DeleteProductUseCase,
    val getProducts: GetProductsUseCase,
)
