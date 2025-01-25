package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.OrderDetailAnalyticsUiState
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductDetailAnalyticViewModel @Inject constructor(
    private val navigator: Navigator,
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailAnalyticsUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getPriceChartDate()
        getAmountChartDate()
    }

    private fun getAmountChartDate() {
        handleResult(orderUseCase.getOrders(),
            onSuccess = { orders ->
                val data = orders.groupBy { it.orderedAt }.mapValues { entry ->
                    entry.value.sumOf { it.total }
                }

                _uiState.update {
                    it.copy(
                        /*amountChartData = AmountChartData(
                            data = data.toSortedMap()
                        )*/
                    )
                }
            },
            onFailure = {

            }
        )
    }

    private fun getPriceChartDate() {
        handleResult(orderUseCase.getOrders(),
            onSuccess = { orders ->
                val data = orders.associate { it.orderedAt to it.total }.toSortedMap()
               /* _uiState.update { state ->
                    state.copy(
                        priceChartData = PriceChartData(data = data)
                    )
                }*/
            },
            onFailure = {

            }
        )
    }
}
