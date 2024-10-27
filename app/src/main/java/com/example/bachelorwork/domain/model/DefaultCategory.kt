package com.example.bachelorwork.domain.model

enum class DefaultCategory {
    WOOD,
    PLASTIC,
    PAPER,
    METAL;

    fun toCategory(): ProductCategory {
        return ProductCategory(
            name = name.lowercase().replaceFirstChar(Char::titlecase)
        )
    }
}
