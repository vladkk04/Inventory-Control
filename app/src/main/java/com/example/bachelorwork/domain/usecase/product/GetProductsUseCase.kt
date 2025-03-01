package com.example.bachelorwork.domain.usecase.product

import androidx.paging.PagingData
import com.example.bachelorwork.domain.model.sorting.SortDirection
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetProductsUseCase(
    private val productRepository: ProductRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        productSortOptions: ProductSortOptions = ProductSortOptions()
    ): Flow<PagingData<Product>> = emptyFlow()


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

            SortBy.QUANTITY -> {
                if (productSortOptions.sortDirection == SortDirection.ASCENDING) {
                    products.sortedBy { it.quantity }
                } else {
                    products.sortedByDescending { it.quantity }
                }
            }
        }
    }
}
