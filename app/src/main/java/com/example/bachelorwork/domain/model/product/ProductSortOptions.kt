package com.example.bachelorwork.domain.model.product

import com.example.bachelorwork.domain.model.SortDirection

data class ProductSortOptions(
    val sortBy: SortBy = SortBy.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING
)