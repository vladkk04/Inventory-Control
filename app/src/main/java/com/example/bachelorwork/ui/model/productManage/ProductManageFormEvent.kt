package com.example.bachelorwork.ui.model.productManage

import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductTag
import com.example.bachelorwork.domain.model.product.ProductUnit

sealed class ProductManageFormEvent {
    data class NameChanged(val name: String) : ProductManageFormEvent()
    data class BarcodeChanged(val barcode: String) : ProductManageFormEvent()
    data class QuantityChanged(val quantity: String) : ProductManageFormEvent()
    data class UnitChanged(val productUnit: ProductUnit) : ProductManageFormEvent()
    data class MinStockLevelChanged(val minStockLevel: String) : ProductManageFormEvent()
    data class CategoryChanged(val category: ProductCategory) : ProductManageFormEvent()
    data class TagsChanged(val tags: List<ProductTag>) : ProductManageFormEvent()
    data class DescriptionChanged(val description: String) : ProductManageFormEvent()
}