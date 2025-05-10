package com.example.inventorycotrol.domain.model.sorting

enum class SortDirection {
    ASCENDING,
    DESCENDING;

    fun opposite(): SortDirection {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }
}