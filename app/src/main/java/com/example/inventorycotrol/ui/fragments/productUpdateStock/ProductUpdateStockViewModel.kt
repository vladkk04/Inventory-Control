// BaseProductUpdateStockViewModel.kt
package com.example.inventorycotrol.ui.fragments.productUpdateStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.remote.mappers.mapToUpdateStock
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.model.updateStock.ProductStockUpdate
import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockItem
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductUpdateStockViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
    private val productUpdateStockUseCases: ProductUpdateStockUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUpdateStockUiState())
    val uiState: StateFlow<ProductUpdateStockUiState> = _uiState.asStateFlow()

    private val _allProducts = MutableStateFlow<List<ProductUpdateStockItem>>(emptyList())

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch(dispatcher) {
            productUseCases.getProducts.getAll().distinctUntilChanged().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _allProducts.value = result.data.map { it.mapToUpdateStock() }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                products = result.data.map { it.mapToUpdateStock() }
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    }

                    Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun updateInputValue(productId: String, newValue: Double?) {
        _allProducts.update { state ->
            state.map {
                if (it.id == productId)
                    it.copy(currentInputValue = newValue ?: 0.00)
                else it
            }
        }
    }

    fun adjustStock(productId: String, isAddition: Boolean) {
        _allProducts.update { state ->
            state.map { item ->
                if (item.id == productId) {
                    val change = if (isAddition) 1.00 else -1.00
                    val newValue = item.currentInputValue + change
                    item.copy(currentInputValue = newValue)
                } else {
                    item
                }
            }
        }
        _uiState.update { state ->
            state.copy(products = _allProducts.value)
        }
    }

    fun updateStock() = viewModelScope.launch {
        val request = ProductUpdateStockRequest(
            productsUpdates = _allProducts.value.filter { it.adjustmentAmount != 0.00 }.map {
                ProductStockUpdate(
                    productId = it.id,
                    previousStock = it.stockOnHand,
                    adjustmentValue = it.adjustmentAmount
                )
            }
        )

        if (request.productsUpdates.isEmpty()) {
            sendSnackbarEvent(SnackbarEvent("No adjustments to make"))
            return@launch
        }

        productUpdateStockUseCases.updateStock(request).onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is ApiResponseResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    navigator.navigateUp()
                }
            }
        }.launchIn(viewModelScope)

    }

    fun searchQuery(query: String?) {
        _uiState.update { state ->
            state.copy(
                products = _allProducts.value.filter {
                    it.name.contains(
                        query ?: "",
                        ignoreCase = true
                    )
                }
            )
        }
    }
}