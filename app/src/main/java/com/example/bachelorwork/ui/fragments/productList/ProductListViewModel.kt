package com.example.bachelorwork.ui.fragments.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        productUseCases.getProducts().onEach { products ->
            _uiState.value = _uiState.value.copy(
                productList = products
            )
        }.launchIn(viewModelScope)
    }


}