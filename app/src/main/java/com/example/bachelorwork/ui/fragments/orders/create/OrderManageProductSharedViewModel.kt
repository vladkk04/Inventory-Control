package com.example.bachelorwork.ui.fragments.orders.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.order.OrderProductSelectedData
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManageProductSharedViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _selectedProduct = MutableSharedFlow<OrderProductSelectedData>()

    val selectedProduct get() = _selectedProduct.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000)
    )

    fun addProduct(product: OrderProductSelectedData) = viewModelScope.launch {
        _selectedProduct.emit(product)
        navigator.navigateUp()
    }

    fun replaceProduct(product: OrderProductSelectedData) = viewModelScope.launch {
        _selectedProduct.emit(product)
        navigator.navigateUp()
    }
}