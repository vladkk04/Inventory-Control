package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductDetailUIState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                Resource.Loading -> Unit
                is Resource.Error -> sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Product deleted"))
                    navigator.navigateUp()
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch{
        navigator.navigateUp()
    }

}