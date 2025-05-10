package com.example.inventorycotrol.domain.model.product

import com.example.inventorycotrol.domain.model.sorting.SortDirection

data class ProductSortOptions(
    val sortBy: SortBy = SortBy.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING
)