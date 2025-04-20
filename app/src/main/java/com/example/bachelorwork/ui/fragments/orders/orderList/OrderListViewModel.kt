package com.example.bachelorwork.ui.fragments.orders.orderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.list.OrderListUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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

    init { getOrders() }

    private fun getOrders() = viewModelScope.launch {
        orderUseCase.getOrders().onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            orders = response.data,
                            isLoading = false
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)

    }

    fun navigateToOrderDetail(id: String) = viewModelScope.launch {
        navigator.navigate(Destination.OrderDetail(id))
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }

    fun openDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }
}