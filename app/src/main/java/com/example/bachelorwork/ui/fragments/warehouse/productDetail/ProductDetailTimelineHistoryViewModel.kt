package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductTimelineHistoryUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailTimelineHistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductUseCases,
) : ViewModel() {

    private val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductTimelineHistoryUiState())

    val uiState = combine(
        productRepository.getProducts.getProductById(productRoute.id),
        _uiState.asStateFlow()
    ) { result, state ->
        handleResult(result, onSuccess = { product ->
            _uiState.value = state.copy(timelineHistory = product.timelineHistory)
        }, onFailure = {

        })
        state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.value)

}