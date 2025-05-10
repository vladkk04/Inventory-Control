package com.example.inventorycotrol.ui.model.product.manage

import com.example.inventorycotrol.domain.model.product.ProductTag
import com.example.inventorycotrol.domain.model.product.ProductUnit

sealed class ProductManageFormEvent {
    data class NameChanged(val name: String) : ProductManageFormEvent()
    data class BarcodeChanged(val barcode: String) : ProductManageFormEvent()
    data class QuantityChanged(val quantity: String) : ProductManageFormEvent()
    data class UnitChanged(val productUnit: ProductUnit) : ProductManageFormEvent()
    data class MinStockLevelChanged(val minStockLevel: String) : ProductManageFormEvent()
    data class CategoryChanged(val categoryId: String) : ProductManageFormEvent()
    data class TagsChanged(val tags: List<ProductTag>) : ProductManageFormEvent()
    data class DescriptionChanged(val description: String) : ProductManageFormEvent()
}