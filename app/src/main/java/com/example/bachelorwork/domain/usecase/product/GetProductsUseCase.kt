package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.pojo.toProduct
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
    fun getProducts(productOrder: ProductOrder): Flow<Result<List<Product>>> {
        return productRepository.getProductsPojo().map { products ->
            val toProducts = products.map { it.toProduct() }
            runCatching { sortProductsByOrder(toProducts, productOrder) }
        }
    }

    fun getProductById(id: Int): Flow<Result<Product>> =
        productRepository.getProductPojoById(id)
            .map { product -> product.runCatching { product.toProduct() } }

    private fun sortProductsByOrder(
        products: List<Product>,
        productOrder: ProductOrder
    ): List<Product> {
        return when (productOrder.sortBy) {
            SortBy.NAME -> {
                if (productOrder.sortDirection == SortDirection.ASCENDING) {
                    products.sortedBy { it.name }
                } else {
                    products.sortedByDescending { it.name }
                }
            }

            SortBy.PRICE -> {
                if (productOrder.sortDirection == SortDirection.ASCENDING) {
                    products.sortedBy { it.pricePerUnit }
                } else {
                    products.sortedByDescending { it.pricePerUnit }
                }
            }
        }
    }
}
