package com.example.inventorycotrol.domain.usecase.product

data class ProductUseCases(
    val createProduct: CreateProductUseCase,
    val updateProduct: UpdateProductUseCase,
    val deleteProduct: DeleteProductUseCase,
    val getProducts: GetProductsUseCase,
)
