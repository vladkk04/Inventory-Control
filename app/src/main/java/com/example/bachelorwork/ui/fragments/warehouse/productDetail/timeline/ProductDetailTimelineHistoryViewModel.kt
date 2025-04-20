package com.example.bachelorwork.ui.fragments.warehouse.productDetail.timeline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.BaseDetailViewModel
import com.example.bachelorwork.ui.model.product.detail.ProductTimelineHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailTimelineHistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productUseCases: ProductUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseDetailViewModel(savedStateHandle, productUseCases, dispatcher) {

    private val _uiState = MutableStateFlow(ProductTimelineHistoryUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch(dispatcher) {
            product.collectLatest { product ->
                if (product != null) {
                    val create = ProductTimelineHistory.ProductTimelineTimelineCreate(
                        createdAt = product.createdAt,
                        createdBy = product.createdBy
                    )

                    val updates = product.updates.map {
                        ProductTimelineHistory.ProductTimelineUpdate(
                            updates = it
                        )
                    }

                    _uiState.update {
                        it.copy(
                            updateHistory = updates + create
                        )
                    }
                }
            }
        }
    }



}