package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.model.Product
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getProducts()
    }
}