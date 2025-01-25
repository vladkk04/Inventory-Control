package com.example.bachelorwork.ui.fragments.orders.orderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.ui.model.order.OrderListUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderUseCase: OrderUseCases,
    val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getOrders()
    }

    private fun getOrders() {
        val result = orderUseCase.getOrders()

        handleResult(result, onSuccess = { orders ->
            _uiState.update { state ->
                state.copy(orders = orders)
            }
        })
    }

    fun navigateToOrderDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.OrderDetail(id))
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }

    fun openDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }
}