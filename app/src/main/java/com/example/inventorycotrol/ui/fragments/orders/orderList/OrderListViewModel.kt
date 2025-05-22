package com.example.inventorycotrol.ui.fragments.orders.orderList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.usecase.order.OrderUseCases
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
import com.example.inventorycotrol.ui.model.order.list.OrderListUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderUseCase: OrderUseCases,
    private val userUseCases: UserUseCases,
    private val navigator: AppNavigator,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getOrders()
        getUser()
        viewModelScope.launch {
            dataStoreManager.getPreference(AppConstants.ORGANISATION_CURRENCY).distinctUntilChanged().collect { currency ->
                _uiState.update { it.copy(currency = AppConstants.CURRENCY_SYMBOLS[currency] ?: "€") }
            }
        }
    }

    private fun getOrders() = viewModelScope.launch {
        orderUseCase.getOrders().distinctUntilChanged().onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    Log.d("debug", response.data.toString())
                    _uiState.update { it.copy(orders = response.data ?: emptyList(), isLoading = false, isRefreshing = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            orders = response.data,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUser() = viewModelScope.launch {
        userUseCases.getUserUseCase.get().distinctUntilChanged().onEach { response ->
            when (response) {
                Resource.Loading -> {

                }
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(imageUrl = response.data.imageUrl)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToOrderDetail(id: String) = viewModelScope.launch {
        navigator.navigate(Destination.OrderDetail(id, uiState.value.currency))
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }

    fun openDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        getOrders()
        getUser()
        viewModelScope.launch {
            dataStoreManager.getPreference(AppConstants.ORGANISATION_CURRENCY).distinctUntilChanged().collect { currency ->
                _uiState.update { it.copy(currency = AppConstants.CURRENCY_SYMBOLS[currency] ?: "€") }
            }
        }
    }
}