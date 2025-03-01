package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.repository.ProductRepository

class UpdateProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: ProductEntity): Result<Unit> {
        return runCatching { productRepository.update(product) }
    }

    suspend operator fun invoke(vararg product: ProductEntity): Result<Unit> {
        return runCatching { productRepository.updateAll(*product) }
    }
}