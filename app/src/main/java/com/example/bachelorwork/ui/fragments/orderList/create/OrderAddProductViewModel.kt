package com.example.bachelorwork.ui.fragments.orderList.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.OrderSelectableProductUi
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderAddProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val validatorNotEmptyUseCase: ValidatorNotEmptyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderAddProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(OrderAddProductFormUiState())
    val uiFormState = _uiFormState.asStateFlow()


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

    fun navigateToProductDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(id)) {
            popUpTo<Destination.Warehouse>()
        }
    }

    fun scanBarcodeScanner() = viewModelScope.launch {
        handleResult(barcodeScannerUseCase(), onSuccess = {
            getProductByBarcode(it.displayValue ?: "")
        })
    }

    fun onEvent(event: OrderAddProductFormEvent) {
        when (event) {
            is OrderAddProductFormEvent.Quantity -> _uiFormState.update {
                it.copy(
                    quantity = event.quantity,
                    quantityError = null
                )
            }

            is OrderAddProductFormEvent.Rate -> _uiFormState.update {
                it.copy(
                    rate = event.rate,
                    rateError = null
                )
            }
        }
    }

    fun addProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        navigator.navigateUp()
    }

    fun selectItem(position: Int) {
        _uiState.update {
            it.copy(
                products = it.products.mapIndexed { index, product ->
                    product.copy(
                        isSelected = index == position
                    )
                }
            )
        }
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

    private fun validateInputs(): Boolean {
        val quantityResult = validatorNotEmptyUseCase(_uiFormState.value.quantity)
        val rateResult = validatorNotEmptyUseCase(_uiFormState.value.rate)

        val hasError = listOf(
            quantityResult,
            rateResult,
        ).any { it.success }

        _uiFormState.update {
            it.copy(
                quantityError = quantityResult.errorMessage,
                rateError = rateResult.errorMessage
            )
        }

        return hasError
    }
}
