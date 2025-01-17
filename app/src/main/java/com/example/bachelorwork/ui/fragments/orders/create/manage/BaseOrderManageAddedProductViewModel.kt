package com.example.bachelorwork.ui.fragments.orders.create.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.ValidatorInputFieldFactory
import com.example.bachelorwork.domain.model.order.OrderProductSelectedData
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorDecimalFormatUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.OrderSelectableProductListUi
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductFormEvent
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductFormUiState
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseOrderManageAddedProductViewModel(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderManageProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(OrderManageProductFormUiState())
    val uiFormState = _uiFormState.asStateFlow()

    @OptIn(FlowPreview::class)
    private val searchQueryFlow = MutableStateFlow("").apply {
        debounce(400).filterNot { it.isEmpty() }.onEach { performSearch(it) }.launchIn(viewModelScope)
    }

    val searchQuery = searchQueryFlow.asStateFlow()

    protected fun performSearch(query: String, ignoreCase: Boolean = true) {
        handleResult(productUseCase.getProducts.getProducts(), onSuccess = { products ->
            val queryProducts = products.filter { it.name.contains(query, ignoreCase = ignoreCase) }
                .map { OrderSelectableProductListUi(it.id, name = it.name, unit = it.unit) }

            _uiState.update {
                it.copy(
                    products = queryProducts,
                    isLoading = false
                )
            }
        })
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                products = emptyList(),
                isSelectedProduct = false,
                isLoading = query.isNotEmpty()
            )
        }
        searchQueryFlow.value = query
    }

    fun navigateToProductDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(id)) {
            popUpTo<Destination.Warehouse>()
        }
    }

    fun scanBarcodeScanner() = viewModelScope.launch {
        handleResult(barcodeScannerUseCase(), onSuccess = {
            getProductByBarcode(it.displayValue ?: "")
            _uiState.update { state ->
                state.copy(
                    products = emptyList(),
                    isSelectedProduct = false
                )
            }
        })
    }

    fun onEvent(event: OrderManageProductFormEvent) {
        when (event) {
            is OrderManageProductFormEvent.Quantity -> _uiFormState.update {
                it.copy(
                    quantity = event.quantity,
                    quantityError = null
                )
            }

            is OrderManageProductFormEvent.Rate -> _uiFormState.update {
                it.copy(
                    rate = event.rate,
                    rateError = null
                )
            }
        }
        
    }

    fun selectItem(productId: Int) {
        if (!_uiState.value.isLoading) {
            _uiState.update { state ->
                state.copy(
                    products = state.products.map { it.copy(isSelected = it.id == productId) },
                    isSelectedProduct = true
                )
            }
        }
    }

    fun getSelectedProductData(): OrderProductSelectedData? {
        if (validateInputs()) return null

        return _uiState.value.products.find { it.isSelected }?.let {
            OrderProductSelectedData(
                productSelectedId = it.id,
                quantity = _uiFormState.value.quantity.toDouble(),
                rate = _uiFormState.value.rate.toDouble()
            )
        }
    }

    private fun getProductByBarcode(barcode: String) = viewModelScope.launch {
        handleResult(productUseCase.getProducts.getProducts(), onSuccess = { products ->
            val queryProducts = products.filter { it.barcode == barcode }
                .map { OrderSelectableProductListUi(it.id, name = it.name, unit = it.unit) }

            _uiState.update {
                it.copy(
                    products = queryProducts,
                    isSelectedProduct = false
                )
            }
        })
    }

    private fun validateInputs(): Boolean {
        val validatorInputFieldFactory = ValidatorInputFieldFactory(
            inputs = arrayOf(
                _uiFormState.value.quantity,
                _uiFormState.value.rate
            ),
            validators = setOf(
                ValidatorNotEmptyUseCase,
                ValidatorDecimalFormatUseCase
            )
        )

        _uiFormState.update {
            it.copy(
                quantityError = validatorInputFieldFactory.errorMessages[_uiFormState.value.quantity],
                rateError = validatorInputFieldFactory.errorMessages[_uiFormState.value.rate]
            )
        }

        return validatorInputFieldFactory.hasError
    }
}