package com.example.bachelorwork.ui.fragments.warehouse.productDetail.timeline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductTimelineHistoryUiState
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailTimelineHistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductUseCases,
) : ViewModel() {

    private val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductTimelineHistoryUiState())

    val uiState = _uiState.asStateFlow()/*combine(
        productRepository.getProducts.getProductById(productRoute.id),
        _uiState.asStateFlow()
    ) { result, state ->
        handleResult(result, onSuccess = { product ->
            _uiState.value = state.copy(timelineHistory = product.timelineHistory)
        }, onFailure = {

        })
        state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.value)*/

}