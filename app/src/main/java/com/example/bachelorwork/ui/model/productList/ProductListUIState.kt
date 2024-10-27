package com.example.bachelorwork.ui.model.productList

import com.example.bachelorwork.domain.model.Product

data class ProductListUIState(
    val productList: List<Product> = emptyList(),
)

fun List<Product>.toProductListUI(): List<ProductListUI> = map {
    ProductListUI(
        name = it.name,
        price = it.price,
        barcode = "32423",
        quantity = 10
    )
}
