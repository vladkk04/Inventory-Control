package com.example.bachelorwork.ui.fragments.orders.create.manage.discount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.bachelorwork.ui.model.order.create.manage.discount.OrderManageDiscountUiState
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderManageDiscountViewModal @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val orderManageDiscountRouteArgs =
        Destination.from<Destination.OrderManageDiscount>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrderManageDiscountUiState())
    val uiState = _uiState.asStateFlow()


    init {
        _uiState.update {
            it.copy(
                discount = orderManageDiscountRouteArgs.discount,
                discountType = orderManageDiscountRouteArgs.discountType
            )
        }
    }

}