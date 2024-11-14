package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) = runCatching {
        productRepository.insert(product)
    }

    suspend operator fun invoke(vararg product: Product) = runCatching {
        productRepository.insertAll(*product)
    }
}