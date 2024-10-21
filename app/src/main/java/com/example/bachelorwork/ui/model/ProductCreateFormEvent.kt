package com.example.bachelorwork.ui.model

import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.model.ProductTag
import com.example.bachelorwork.domain.model.ProductUnit

sealed class ProductCreateFormEvent {
    data object CreateProduct : ProductCreateFormEvent()
    data object QuantityIncreased : ProductCreateFormEvent()
    data object QuantityDecreased : ProductCreateFormEvent()
    data object StartScanBarcode : ProductCreateFormEvent()

    data class NameChanged(val name: String) : ProductCreateFormEvent()
    data class BarcodeChanged(val barcode: String) : ProductCreateFormEvent()
    data class QuantityChanged(val quantity: String) : ProductCreateFormEvent()
    data class UnitChanged(val unit: ProductUnit) : ProductCreateFormEvent()
    data class PricePerUnitChanged(val pricePerUnit: String) : ProductCreateFormEvent()
    data class DatePurchaseChanged(val datePurchase: String) : ProductCreateFormEvent()
    data class MinStockLevelChanged(val minStockLevel: String) : ProductCreateFormEvent()
    data class CategoryChanged(val category: String) : ProductCreateFormEvent()
    data class TagsChanged(val tags: List<ProductTag>) : ProductCreateFormEvent()
    data class DescriptionChanged(val description: String) : ProductCreateFormEvent()
}