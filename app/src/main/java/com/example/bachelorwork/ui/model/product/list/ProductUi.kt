package com.example.bachelorwork.ui.model.product.list

import android.net.Uri
import com.example.bachelorwork.domain.model.sorting.SortDirection
import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.domain.model.product.SortBy

data class ProductUi(
    val id: Int,
    val image: Uri? = null,
    val name: String,
    val barcode: String,
    val category: String? = null,
    val minStockLevel: Double,
    val unit: ProductUnit,
    val quantity: Double
)

fun List<ProductUi>.sortBy(sortOptions: ProductSortOptions) =
    when (sortOptions.sortBy) {
        SortBy.NAME -> {
            if (sortOptions.sortDirection == SortDirection.ASCENDING) {
                this.sortedBy { it.name }
            } else {
                this.sortedByDescending { it.name }
            }
        }

        SortBy.QUANTITY -> {
            if (sortOptions.sortDirection == SortDirection.ASCENDING) {
                this.sortedBy { it.quantity }
            } else {
                this.sortedByDescending { it.quantity }
            }
        }
    }