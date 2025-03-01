package com.example.bachelorwork.ui.fragments.orders.orderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.list.OrderListUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
    val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getOrders()
    }

    private fun getOrders() {

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