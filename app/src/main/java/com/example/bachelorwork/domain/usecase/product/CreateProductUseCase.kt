package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.repository.ProductRepository

class CreateProductUseCase (
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(product: ProductEntity) = runCatching {
        productRepository.insert(product)
    }

    suspend operator fun invoke(vararg product: ProductEntity) = runCatching {
        productRepository.insertAll(*product)
    }
}