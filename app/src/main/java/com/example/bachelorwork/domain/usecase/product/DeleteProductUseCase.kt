package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) = runCatching {
        productRepository.delete(product)
    }

    suspend operator fun invoke(vararg product: Product) = runCatching {
        productRepository.deleteAll(*product)
    }

}