package com.example.bachelorwork.ui.fragments.orders.orderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    val navigator: Navigator
) : ViewModel() {

    fun navigateToOrderDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.OrderDetail(id))
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }
}