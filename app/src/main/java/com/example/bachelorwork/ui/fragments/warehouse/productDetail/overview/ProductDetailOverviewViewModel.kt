package com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductDetailUIState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductUseCases,
    private val navigator: AppNavigator
) : ViewModel() {

    private val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductDetailUIState())

    val uiState = _uiState.asStateFlow()/* combine(
        productRepository.getProducts.getProductById(productRoute.id),
        _uiState.asStateFlow()
    ) { result, state ->
        handleResult(result, onSuccess = { product ->
            _uiState.value = state.copy(product = product)
        }, onFailure = {
            Log.d("tag", it.message.toString())
        })
        state
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _uiState.value)*/



    fun editProduct() = viewModelScope.launch {
        navigator.navigate(Destination.EditProduct(productRoute.id))
    }

    fun deleteProduct() = viewModelScope.launch {
        /*val result = productRepository.deleteProduct(productRoute.id)
        handleResult(result,
            onSuccess = {
                viewModelScope.launch {
                    appNavigator.navigateUp()
                }
            }, onFailure = {

            }
        )*/
    }

    fun navigateBack() = viewModelScope.launch{
        navigator.navigateUp()
    }

}