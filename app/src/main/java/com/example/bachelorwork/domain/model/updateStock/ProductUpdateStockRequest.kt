package com.example.bachelorwork.domain.model.updateStock
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductUpdateStockRequest(
    @SerialName("products")
    val productsUpdates: List<ProductStockUpdate>,
    @SerialName("operation_type")
    val stockOperationType: StockOperationType,
)
