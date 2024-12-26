package com.example.bachelorwork.domain.usecase.product

import com.example.bachelorwork.data.local.pojo.toProduct
import com.example.bachelorwork.domain.model.SortDirection
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    fun getProducts(productSortOptions: ProductSortOptions): Flow<Result<List<Product>>> {
        return productRepository.getProductsPojo().map { products ->
            val toProducts = products.map { it.toProduct() }
            runCatching { sortProductsByOrder(toProducts, productSortOptions) }
        }
    }

    fun getProductById(id: Int): Flow<Result<Product>> =
        productRepository.getProductPojoById(id)
            .map { product -> product.runCatching { product.toProduct() } }

    private fun sortProductsByOrder(
        products: List<Product>,
        productSortOptions: ProductSortOptions
    ): List<Product> {
        return when (productSortOptions.sortBy) {
            SortBy.NAME -> {
                if (productSortOptions.sortDirection == SortDirection.ASCENDING) {
                    products.sortedBy { it.name }
                } else {
                    products.sortedByDescending { it.name }
                }
            }
        }
    }
}
