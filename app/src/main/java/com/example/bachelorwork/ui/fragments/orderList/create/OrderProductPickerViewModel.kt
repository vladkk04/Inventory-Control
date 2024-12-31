package com.example.bachelorwork.ui.fragments.orderList.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.OrderSelectableProductUi
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderProductPickerViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderProductPickerUiState())
    val uiState = _uiState.asStateFlow()

    fun searchProductByName(query: String) = viewModelScope.launch {
        handleResult(productUseCase.getProducts.getProducts(), onSuccess = { products ->
            val queryProducts = products.filter { it.name.contains(query, ignoreCase = true) }
                .map { OrderSelectableProductUi(it.id, name = it.name) }

            _uiState.value = _uiState.value.copy(
                searchQuery = query,
                products = queryProducts
            )
        })
    }

    private fun getProductByBarcode(barcode: String) = viewModelScope.launch {
        handleResult(productUseCase.getProducts.getProducts(), onSuccess = { products ->
            val queryProducts = products.filter { it.barcode == barcode }
                .map { OrderSelectableProductUi(it.id, name = it.name) }

            _uiState.value = _uiState.value.copy(
                searchQuery = barcode,
                products = queryProducts
            )
        })
    }

    fun navigateToProductDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(0))
    }

    fun scanBarcodeScanner() = viewModelScope.launch {
        handleResult(barcodeScannerUseCase(), onSuccess = {
            getProductByBarcode(it.displayValue ?: "")
        })
    }
}
