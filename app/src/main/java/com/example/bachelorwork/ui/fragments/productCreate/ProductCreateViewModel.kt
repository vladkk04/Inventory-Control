package com.example.bachelorwork.ui.fragments.productCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.CreateProductUseCase
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.model.productCreate.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.productCreate.ProductCreateFormState
import com.example.bachelorwork.ui.model.productCreate.ProductCreateUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val startBarcodeScannerUseCase: BarcodeScannerUseCase,
    private val validatorNotEmptyUseCase: ValidatorNotEmptyUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val categoryUseCase: ProductCategoryUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductCreateUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductCreateFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        handleResult(categoryUseCase.getCategories()) {
            _uiState.update { state -> state.copy(categories = it) }
        }
    }

    fun createProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        val result = createProductUseCase(Product(uiFormState.value))

        handleResult(result) {
            _uiState.update { it.copy(isProductCreated = true) }
        }
    }

    fun createCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.createCategory(category)
        handleResult(result)
    }

    fun updateCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.updateCategory(category)
        handleResult(result)
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.deleteCategory(category)
        handleResult(result)
    }

    fun increaseQuantity() {
        _uiFormState.update { it.increaseQuantity() }
    }

    fun decreaseQuantity() {
        _uiFormState.update { it.decreaseQuantity() }
    }

    fun startScanBarcode() {
        handleResult(startBarcodeScannerUseCase()) { barcode ->
            _uiState.update { it.copy(barcode = barcode.rawValue!!) }
        }
    }

    fun onEvent(event: ProductCreateFormEvent) {
        when (event) {
            is ProductCreateFormEvent.NameChanged -> {
                _uiFormState.update { it.copy(name = event.name, nameError = null) }
            }

            is ProductCreateFormEvent.BarcodeChanged -> {
                _uiFormState.update { it.copy(barcode = event.barcode, barcodeError = null) }
            }

            is ProductCreateFormEvent.QuantityChanged -> {
                event.quantity.toIntOrNull()
                    ?.let { quantity -> _uiFormState.update { it.copy(quantity = quantity) } }
            }

            is ProductCreateFormEvent.UnitChanged -> {
                _uiFormState.update { it.copy(productUnit = event.productUnit) }
            }

            is ProductCreateFormEvent.PricePerUnitChanged -> {
                _uiFormState.update {
                    it.copy(
                        pricePerUnit = event.pricePerUnit,
                        pricePerUnitError = null
                    )
                }
            }

            is ProductCreateFormEvent.DatePurchaseChanged -> {
                _uiFormState.update {
                    it.copy(
                        datePurchase = event.datePurchase,
                        datePurchaseError = null
                    )
                }
            }

            is ProductCreateFormEvent.CategoryChanged -> {
                _uiFormState.update {
                    it.copy(
                        category = event.category,
                        categoryError = null
                    )
                }
            }

            is ProductCreateFormEvent.MinStockLevelChanged -> {
                _uiFormState.update {
                    it.copy(
                        minStockLevel = event.minStockLevel,
                        minStockLevelError = null
                    )
                }
            }

            is ProductCreateFormEvent.TagsChanged -> {
                _uiFormState.update { it.copy(tags = event.tags) }
            }

            is ProductCreateFormEvent.DescriptionChanged -> {
                _uiFormState.update { it.copy(description = event.description) }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val nameResult = validatorNotEmptyUseCase(_uiFormState.value.name)
        val barcodeResult = validatorNotEmptyUseCase(_uiFormState.value.barcode)
        val pricePerUnit = validatorNotEmptyUseCase(_uiFormState.value.pricePerUnit)
        val datePurchase = validatorNotEmptyUseCase(_uiFormState.value.datePurchase)
        val minStockLevel = validatorNotEmptyUseCase(_uiFormState.value.minStockLevel)
        val category = validatorNotEmptyUseCase(_uiFormState.value.category.name)

        val hasError = listOf(
            nameResult,
            barcodeResult,
            pricePerUnit,
            datePurchase,
            minStockLevel,
            category
        ).any { it.success }

        _uiFormState.update {
            it.copy(
                nameError = nameResult.errorMessage,
                barcodeError = barcodeResult.errorMessage,
                pricePerUnitError = pricePerUnit.errorMessage,
                datePurchaseError = datePurchase.errorMessage,
                minStockLevelError = minStockLevel.errorMessage,
                categoryError = category.errorMessage
            )
        }

        return hasError
    }

    private fun <T> handleResult(
        result: Result<T>,
        onSuccess: ((T) -> Unit)? = null,
    ) {
        result.fold(
            onSuccess = {
                onSuccess?.invoke(it)
            },
            onFailure = { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        )
    }

    private fun <T> handleResult(
        flowResult: Flow<Result<T>>,
        onSuccess: ((T) -> Unit)? = null,
    ) {
        flowResult.onEach { result ->
            handleResult(result, onSuccess)
        }.launchIn(viewModelScope)
    }
}

