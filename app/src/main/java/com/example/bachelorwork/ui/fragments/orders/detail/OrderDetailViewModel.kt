package com.example.bachelorwork.ui.fragments.orders.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.detail.OrderDetailUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderNavigationArgs = Destination.from<Destination.OrderDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    init { getOrder() }

    private fun getOrder() {

    }

    fun navigateToOrderEdit() = viewModelScope.launch {
        navigator.navigate(Destination.EditOrder(orderNavigationArgs.id))
    }

    fun deleteOrder() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

}