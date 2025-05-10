package com.example.inventorycotrol.domain.model.updateStock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductUpdateStockRequest(
    @SerialName("products")
    val productsUpdates: List<ProductStockUpdate>,
    @SerialName("order_id")
    val orderId: String? = null
)
