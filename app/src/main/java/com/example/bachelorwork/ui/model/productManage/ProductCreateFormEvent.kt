package com.example.bachelorwork.ui.model.productManage

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit

sealed class ProductCreateFormEvent {
    data class NameChanged(val name: String) : ProductCreateFormEvent()
    data class BarcodeChanged(val barcode: String) : ProductCreateFormEvent()
    data class QuantityChanged(val quantity: String) : ProductCreateFormEvent()
    data class UnitChanged(val productUnit: ProductUnit) : ProductCreateFormEvent()
    data class PricePerUnitChanged(val pricePerUnit: String) : ProductCreateFormEvent()
    data class DatePurchaseChanged(val datePurchase: String) : ProductCreateFormEvent()
    data class MinStockLevelChanged(val minStockLevel: String) : ProductCreateFormEvent()
    data class CategoryChanged(val category: ProductCategory) : ProductCreateFormEvent()
    data class TagsChanged(val tags: List<ProductTag>) : ProductCreateFormEvent()
    data class DescriptionChanged(val description: String) : ProductCreateFormEvent()
}