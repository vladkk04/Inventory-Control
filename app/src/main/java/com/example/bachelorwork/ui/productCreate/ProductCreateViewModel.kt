package com.example.bachelorwork.ui.productCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resources
import com.example.bachelorwork.domain.model.ProductCreateUIState
import com.example.bachelorwork.domain.usecase.BarcodeScannerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val startBarcodeScannerUseCase: BarcodeScannerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductCreateUIState())
    val uiState get() = _uiState.asStateFlow()

    fun setQuantity(quantity: Int?) {
        _uiState.update { it.copy(quantity = quantity ?: it.quantity) }
        calculateTotalPrice()
    }

    fun setPrice(price: Double?) {
        _uiState.update { it.copy(price = price ?: 0.00)}
        calculateTotalPrice()
    }

    fun setDatePurchase(datePurchase: String) {
        _uiState.update { it.copy(datePurchase = datePurchase) }
    }

    fun increaseQuantity() = _uiState.value++
    fun decreaseQuantity() = _uiState.value--

    private fun calculateTotalPrice() {
        _uiState.update { it.copy(totalPrice = it.quantity * it.price) }
    }

    fun startScanBarcode() = viewModelScope.launch {
        startBarcodeScannerUseCase().collectLatest { result ->
            when (result) {
                is Resources.Success -> {
                    _uiState.update { it.copy(barcode = result.data?.rawValue) }
                }
                is Resources.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
                else -> {}
            }
        }
    }
}
