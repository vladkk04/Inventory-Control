package com.example.bachelorwork.ui.fragments.orders.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.model.order.manage.OrderManageUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


abstract class BaseOrderManageViewModel(
    private val navigator: AppNavigator
): ViewModel() {

    private val _uiState = MutableStateFlow(OrderManageUiState())

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        navigator.navigate(Destination.OrderAddProduct)
    }

    fun navigateToOrderManageDiscount(discountType: DiscountType) = viewModelScope.launch {
        /*_uiState.update { state ->
            state.copy(
                discount = 0.00,
                discountType = discountType,
                total = calculateTotal(state.subtotal, state.discount, discountType)
            )
        }*/
        navigator.navigate(
            Destination.OrderManageDiscount(
                discount = 0.00,
                discountType = discountType
            )
        )
    }
}