package com.example.inventorycotrol.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
): ViewModel() {

    private val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState = _uiState.asStateFlow()

    init { getProductDetail() }

    private fun getProductDetail() = viewModelScope.launch {
        productUseCases.getProducts.getById(productRoute.id).distinctUntilChanged().collectLatest { response ->
            //Log.d("debug", response.toString())
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, product = response.data) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, product = response.data) }
                }
            }
        }
    }


    fun editProduct() = viewModelScope.launch {
        navigator.navigate(Destination.EditProduct(productRoute.id))
    }

    fun deleteProduct() = viewModelScope.launch {
        productUseCases.deleteProduct(productRoute.id).distinctUntilChanged().onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent("Product deleted"))
                    navigator.navigateUp()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch{
        navigator.navigateUp()
    }

}