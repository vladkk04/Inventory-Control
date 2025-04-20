package com.example.bachelorwork.ui.fragments.orders.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.order.OrderProductSelectedData
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManageProductSharedViewModel @Inject constructor(
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _selectedProduct = MutableStateFlow<OrderProductSelectedData?>(null)

    val selectedProduct
        get() = _selectedProduct.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000)
        )

    fun addProduct(product: OrderProductSelectedData) = viewModelScope.launch {
        _selectedProduct.update { product }
        navigator.navigateUp()
    }
}