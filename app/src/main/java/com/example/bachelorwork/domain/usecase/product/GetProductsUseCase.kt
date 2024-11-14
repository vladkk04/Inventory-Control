package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductOrder
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.product.SortDirection
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productOrder: ProductOrder): Flow<Result<List<Product>>> {
        return productRepository.getAll().map { products ->
            products.runCatching {
                val sortedProducts: List<Product> = when (productOrder.sortBy) {
                    SortBy.NAME -> {
                        when (productOrder.sortDirection) {
                            SortDirection.ASCENDING -> sortedBy { it.name }
                            SortDirection.DESCENDING -> sortedByDescending { it.name }
                        }
                    }
                    SortBy.PRICE -> {
                        when (productOrder.sortDirection) {
                            SortDirection.ASCENDING -> sortedBy { it.price }
                            SortDirection.DESCENDING -> sortedByDescending { it.price }
                        }
                    }
                }
                sortedProducts
            }
        }
    }

    operator fun invoke(id: Int): Flow<Result<Product>> =
        productRepository.getProductById(id).map { runCatching { it } }

}