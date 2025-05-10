package com.example.inventorycotrol.ui.model.home

import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockCompact

data class HomeUiState(
    val criticalStockItems: List<Product> = emptyList(),
    val productUpdateStockItems: List<ProductUpdateStockCompact> = emptyList(),
    val notificationSettings: NotificationSettings? = null,
    val thresholdSettings: ThresholdSettings? = null,
    val totalUsersCount: Int = 0,
    val totalProductsCount: Int = 0,
    val filterCriticalItems: FilterCriticalItems = FilterCriticalItems.LAST_10,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)

enum class FilterCriticalItems {
    SHOW_ALL,
    LAST_100,
    LAST_10,
    LAST_50
}
