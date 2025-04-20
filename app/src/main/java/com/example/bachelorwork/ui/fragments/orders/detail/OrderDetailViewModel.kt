package com.example.bachelorwork.ui.fragments.orders.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.detail.OrderDetailUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val orderUseCase: OrderUseCases,
    private val productUseCase: ProductUseCases,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val orderNavigationArgs = Destination.from<Destination.OrderDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getOrder()
    }

    private fun getOrder() = viewModelScope.launch {
        orderUseCase.getOrders.invoke(orderNavigationArgs.id).onEach { response ->
            when (response) {
                Resource.Loading -> {

                }

                is Resource.Error -> {

                }

                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            order = response.data
                        )
                    }
                }
            }
        }.flowOn(ioDispatcher).launchIn(viewModelScope)
    }

    fun navigateToOrderEdit() = viewModelScope.launch {
        navigator.navigate(Destination.EditOrder(orderNavigationArgs.id))
    }

    fun deleteOrder() = viewModelScope.launch {
        orderUseCase.deleteOrder(orderNavigationArgs.id).onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    navigator.navigateUp()
                    sendSnackbarEvent(SnackbarEvent("Order deleted"))
                }
            }
        }.flowOn(ioDispatcher).launchIn(viewModelScope)
    }

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

}