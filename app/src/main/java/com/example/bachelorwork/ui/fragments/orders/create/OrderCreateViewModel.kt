package com.example.bachelorwork.ui.fragments.orders.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.model.order.OrderAddedProductUi
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderCreateUiState())
    val uiState = _uiState.asStateFlow()

    fun addProductToOrder(product: OrderAddedProductUi) {
        _uiState.update {
            it.copy(
                addedProduct = it.addedProduct + product
            )
        }
    }

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        navigator.navigate(Destination.OrderAddProduct)
    }
}
