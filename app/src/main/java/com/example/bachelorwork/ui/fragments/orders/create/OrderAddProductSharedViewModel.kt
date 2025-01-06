package com.example.bachelorwork.ui.fragments.orders.create

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderAddProductSharedViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct get() = _selectedProduct.asStateFlow()

    /*fun addProduct() = viewModelScope.launch {
        navigator.navigateUp()
    }*/


}