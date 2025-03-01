package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(product: ProductEntity) = runCatching {
        productRepository.delete(product)
    }

    suspend operator fun invoke(vararg product: ProductEntity) = runCatching {
        productRepository.deleteAll(*product)
    }


}