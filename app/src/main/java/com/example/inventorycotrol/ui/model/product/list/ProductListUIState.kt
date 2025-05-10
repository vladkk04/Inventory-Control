package com.example.inventorycotrol.ui.model.product.list

import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.model.product.ProductSortOptions
import com.example.inventorycotrol.domain.model.product.ProductViewDisplayMode
import com.example.inventorycotrol.domain.model.product.SortBy
import com.example.inventorycotrol.domain.model.sorting.SortDirection
import com.example.inventorycotrol.ui.fragments.warehouse.filters.SharedWarehouseFilterUiState
import com.example.inventorycotrol.ui.fragments.warehouse.filters.StockFilter

data class ProductListUIState(
    val products: List<Product> = emptyList(),
    val filtersCount: Int = 0,
    val imageUrl: String? = null,
    val viewDisplayType: ProductViewDisplayMode = ProductViewDisplayMode.ROW,
    val sortOptions: ProductSortOptions = ProductSortOptions(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
fun List<Product>.sortBy(sortOptions: ProductSortOptions): List<Product> {
    return when (sortOptions.sortBy) {
        SortBy.NAME -> sortedByName(sortOptions.sortDirection)
        SortBy.QUANTITY -> sortedByQuantity(sortOptions.sortDirection)
    }
}

private fun List<Product>.sortedByName(direction: SortDirection): List<Product> {
    return if (direction == SortDirection.ASCENDING) {
        sortedBy { it.name.lowercase() }
    } else {
        sortedByDescending { it.name.lowercase() }
    }
}

fun List<Product>.moreFilters(sharedWarehouseFilterUiState: SharedWarehouseFilterUiState): List<Product> {
    return filter { product ->
        (sharedWarehouseFilterUiState.categoryFilters.isEmpty() ||
                sharedWarehouseFilterUiState.categoryFilters.any { it.name == product.categoryName }) &&

                (sharedWarehouseFilterUiState.tags.isEmpty() ||
                        product.tags.any { tag ->
                            sharedWarehouseFilterUiState.tags.contains(
                                tag
                            )
                        }) &&

                (sharedWarehouseFilterUiState.stockFilters.isEmpty() ||
                        sharedWarehouseFilterUiState.stockFilters.any { stockFilter ->
                            when (stockFilter) {
                                StockFilter.OVERSTOCK -> product.quantity >= product.minStockLevel
                                StockFilter.LOW_STOCK -> product.quantity <= product.minStockLevel * 0.75
                                        && product.quantity > product.minStockLevel * 0.25

                                StockFilter.CRITICAL_STOCK -> product.quantity <= product.minStockLevel * 0.25
                                        && product.quantity > 0

                                StockFilter.OUT_OF_STOCK -> product.quantity == 0.00
                            }
                        })
    }
}

private fun List<Product>.sortedByQuantity(direction: SortDirection): List<Product> {
    return if (direction == SortDirection.ASCENDING) {
        sortedBy { it.quantity }
    } else {
        sortedByDescending { it.quantity }
    }
}

