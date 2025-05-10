package com.example.inventorycotrol.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.model.product.detail.ProductDetailUIState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productUseCases: ProductUseCases,
    private val navigator: AppNavigator,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): BaseDetailViewModel(savedStateHandle, productUseCases, dispatcher) {


    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState = _uiState.asStateFlow()

    fun editProduct() = viewModelScope.launch {
        navigator.navigate(Destination.EditProduct(productRoute.id))
    }

    fun deleteProduct() = viewModelScope.launch {
        productUseCases.deleteProduct(productRoute.id).onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _isLoading.update { true }
                }
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    _isLoading.update { false }
                }
                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Product deleted"))
                    _isLoading.update { false }
                    navigator.navigateUp()
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch{
        navigator.navigateUp()
    }

}