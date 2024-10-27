package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.model.Product
import com.example.bachelorwork.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.deleteProduct(product)
    }
}