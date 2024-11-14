package com.example.bachelorwork.domain.model.product

data class ProductOrder(
    val sortBy: SortBy = SortBy.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING
)