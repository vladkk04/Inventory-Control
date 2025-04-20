// BaseProductUpdateStockViewModel.kt
package com.example.bachelorwork.ui.fragments.productUpdateStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.remote.mappers.mapToSelection
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.domain.model.updateStock.ProductStockUpdate
import com.example.bachelorwork.domain.model.updateStock.ProductUpdateStockRequest
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.bachelorwork.ui.model.order.SelectableProductUi
import com.example.bachelorwork.ui.model.productUpdateStock.StockAdjustmentUi
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseProductUpdateStockViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
    private val productUpdateStockUseCases: ProductUpdateStockUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUpdateStockUiState())
    val uiState: StateFlow<ProductUpdateStockUiState> = _uiState.asStateFlow()

    private var _operationType = StockOperationType.STOCK_IN

    private val _enteredQuantity = MutableStateFlow("")

    private val _allProducts = MutableStateFlow<List<SelectableProductUi>>(emptyList())


    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch(dispatcher) {
            productUseCases.getProducts.getAll().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _allProducts.value = result.data.map { it.mapToSelection() }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                products = result.data.map { it.mapToSelection() }
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

    fun setOperationType(type: StockOperationType) {
        _operationType = type
    }

    fun setEnteredQuantity(quantity: String) {
        _enteredQuantity.value = quantity
        validateInput()
    }

    fun selectProduct(product: SelectableProductUi) {
        _uiState.update { state ->
            state.copy(
                products = state.products.map {
                    it.copy(isSelected = it.id == product.id)
                }
            )
        }
        validateInput()
    }

    fun addAdjustment() {
        val selectedProduct = _uiState.value.products.find { it.isSelected } ?: return
        val quantity = _enteredQuantity.value.toDoubleOrNull() ?: return

        _uiState.update { state ->
            val existingAdjustment = state.adjustments.find { it.productId == selectedProduct.id }

            val newAdjustments = if (existingAdjustment != null) {
                state.adjustments.map {
                    if (it.productId == selectedProduct.id) {
                        it.copy(adjustedAmount = it.adjustedAmount + quantity)
                    } else it
                }
            } else {
                state.adjustments + StockAdjustmentUi(
                    productId = selectedProduct.id,
                    productName = selectedProduct.name,
                    currentStock = selectedProduct.currentStock,
                    adjustedAmount = quantity,
                    imageUrl = selectedProduct.image,
                    unit = selectedProduct.unit.name.lowercase()
                )
            }

            state.copy(
                adjustments = newAdjustments,
                products = state.products.map {
                    if (it.id == selectedProduct.id) it.copy(isSelected = false) else it
                }
            )
        }

        _enteredQuantity.value = ""
        validateInput()
    }

    fun removeAdjustment(adjustment: StockAdjustmentUi) {
        _uiState.update { state ->
            state.copy(adjustments = state.adjustments - adjustment)
        }
    }

    fun updateStock() = viewModelScope.launch {
        val request = ProductUpdateStockRequest(
            productsUpdates = _uiState.value.adjustments.map {
                ProductStockUpdate(
                    productId = it.productId,
                    previousStock = it.currentStock,
                    adjustmentValue = it.adjustedAmount
                )
            },
            stockOperationType = _operationType,
        )

        if (request.productsUpdates.isEmpty()) {
            sendSnackbarEvent(SnackbarEvent("No adjustments to make"))
            return@launch
        }

        productUpdateStockUseCases.updateStock(request).onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {

                }
                is ApiResponseResult.Failure -> {

                }

                is ApiResponseResult.Success -> {
                    navigator.navigateUp()
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)

    }

    private fun validateInput() {
        val quantity = _enteredQuantity.value.toDoubleOrNull() ?: 0.0
        val selectedProduct = _uiState.value.products.find { it.isSelected }

        val existingAdjustments = _uiState.value.adjustments
            .filter { it.productId == selectedProduct?.id }
            .sumOf { it.adjustedAmount }

        val isValid = when {
            selectedProduct == null -> false
            quantity <= 0 -> false
            _operationType == StockOperationType.STOCK_OUT -> {
                val availableStock = selectedProduct.currentStock - existingAdjustments
                quantity <= availableStock
            }
            (selectedProduct.unit == ProductUnit.PCS || selectedProduct.unit == ProductUnit.BOX) &&
                    quantity % 1 != 0.0 -> false
            else -> true
        }

        _uiState.update { it.copy(canAddAdjustment = isValid) }

        if (_operationType == StockOperationType.STOCK_OUT && selectedProduct != null &&
            quantity > (selectedProduct.currentStock - existingAdjustments)) {
            val available = selectedProduct.currentStock - existingAdjustments
            sendSnackbarEvent(SnackbarEvent("Available stock: $available ${selectedProduct.unit.name.lowercase()}"))
        }
    }

    fun searchQuery(query: String?) {
        _uiState.update { state ->
            state.copy(
                products = _allProducts.value.filter { it.name.contains(query ?: "", ignoreCase = true) }
            )
        }
    }
}