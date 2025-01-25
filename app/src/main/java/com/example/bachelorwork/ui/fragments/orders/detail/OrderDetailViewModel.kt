package com.example.bachelorwork.ui.fragments.orders.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.ui.model.order.OrderDetailUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val navigator: Navigator,
    private val orderUseCase: OrderUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderNavigationArgs = Destination.from<Destination.OrderDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    init { getOrder() }

    private fun getOrder() {
         handleResult(orderUseCase.getOrders(orderNavigationArgs.id),
             onSuccess = { order ->
                 _uiState.update { it.copy(order = order) }
             }, onFailure = {
                 _uiState.update { it.copy(errorMessage = it.errorMessage) }
             }
         )
    }

    fun deleteOrder() = viewModelScope.launch {
        handleResult(orderUseCase.deleteOrder.invoke(orderNavigationArgs.id),
            onSuccess = {
                sendSnackbarEvent(SnackbarEvent("Order deleted"))
            }, onFailure = {

            }
        )

        navigator.navigateUp()
    }

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

}