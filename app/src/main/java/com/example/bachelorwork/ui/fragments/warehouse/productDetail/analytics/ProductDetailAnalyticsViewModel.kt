package com.example.bachelorwork.ui.fragments.warehouse.productDetail.analytics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.detail.OrderDetailAnalyticsUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailAnalyticsViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailAnalyticsUiState())
    val uiState get() = _uiState.asStateFlow()

    private val productRouteArg = Destination.from<Destination.ProductDetail>(savedStateHandle)

    init {
        getPriceChartDate()
        getAmountChartDate()
    }

    private fun getAmountChartDate() {

    }

    private fun getPriceChartDate() {



    }
}
