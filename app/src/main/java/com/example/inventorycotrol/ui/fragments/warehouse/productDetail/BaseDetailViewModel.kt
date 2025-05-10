package com.example.inventorycotrol.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val productUseCases: ProductUseCases,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    protected val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    val product = MutableStateFlow<Product?>(null)
    val productState = product.asStateFlow()

    protected val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getProductDetail()
    }


    private fun getProductDetail() = viewModelScope.launch(dispatcher) {
        productUseCases.getProducts.getById(productRoute.id).onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _isLoading.update { true }
                }
                is Resource.Error -> {
                    _isLoading.update { false }
                    sendSnackbarEvent(SnackbarEvent("Product deleted"))
                }
                is Resource.Success -> {
                    product.update { response.data }
                    _isLoading.update { false }
                }
            }
        }.launchIn(viewModelScope)
    }



}