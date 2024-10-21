package com.example.bachelorwork.ui.productCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resources
import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.category.CategoryUseCases
import com.example.bachelorwork.domain.usecase.inputFieldValidation.ValidateNonEmptyFieldUseCase
import com.example.bachelorwork.ui.model.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.ProductCreateFormState
import com.example.bachelorwork.ui.model.ProductCreateUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val startBarcodeScannerUseCase: BarcodeScannerUseCase,
    private val validateNonEmptyFieldUseCase: ValidateNonEmptyFieldUseCase,
    private val categoryUseCase: CategoryUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductCreateUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductCreateFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        categoryUseCase.getCategory().onSuccess { result ->
            _uiState.update { it.copy(categories = result) }
        }
    }

    fun createCategory(name: String) = viewModelScope.launch {
        categoryUseCase.createCategory(name).collectLatest { newCategory ->
            newCategory.onSuccess { result ->
                _uiState.update { it.copy(categories = it.categories + result) }
            }
        }
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        categoryUseCase.deleteCategory(category)
    }

    fun onEvent(event: ProductCreateFormEvent) {
        when (event) {
            ProductCreateFormEvent.CreateProduct -> {
                createNewProduct()
            }

            ProductCreateFormEvent.QuantityDecreased -> {
                _uiFormState.update { it.copy(quantity = it.quantity + 1) }
            }

            ProductCreateFormEvent.QuantityIncreased -> {
                _uiFormState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1)) }
            }

            ProductCreateFormEvent.StartScanBarcode -> {
                startScanBarcode()
            }

            is ProductCreateFormEvent.NameChanged -> {
                _uiFormState.update { it.copy(name = event.name, nameError = null) }
            }

            is ProductCreateFormEvent.BarcodeChanged -> {
                _uiFormState.update { it.copy(barcode = event.barcode, barcodeError = null) }
            }

            is ProductCreateFormEvent.QuantityChanged -> {
                if (_uiFormState.value.quantity != event.quantity.toIntOrNull()) {
                    _uiFormState.update {
                        it.copy(
                            quantity = event.quantity.toIntOrNull() ?: _uiFormState.value.quantity
                        )
                    }
                }
            }

            is ProductCreateFormEvent.UnitChanged -> {
                _uiFormState.update { it.copy(unit = event.unit) }
            }

            is ProductCreateFormEvent.PricePerUnitChanged -> {
                _uiFormState.update {
                    it.copy(
                        pricePerUnit = event.pricePerUnit.toDoubleOrNull() ?: 0.00,
                    )
                }
            }

            is ProductCreateFormEvent.DatePurchaseChanged -> {
                _uiFormState.update {
                    it.copy(
                        datePurchase = event.datePurchase, datePurchaseError = null
                    )
                }
            }

            is ProductCreateFormEvent.CategoryChanged -> {
               /* _uiFormState.update {
                    it.copy(
                        category = ProductCategory(event.category), categoryError = null
                    )
                }*/
            }

            is ProductCreateFormEvent.MinStockLevelChanged -> {
                _uiFormState.update {
                    it.copy(
                        minStockLevel = event.minStockLevel, minStockLevelError = null
                    )
                }
            }

            is ProductCreateFormEvent.TagsChanged -> {
                _uiFormState.update {
                    it.copy(
                        tags = event.tags
                    )
                }
            }

            is ProductCreateFormEvent.DescriptionChanged -> {
                _uiFormState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
        }
    }

    private fun startScanBarcode() = viewModelScope.launch {
        startBarcodeScannerUseCase().collectLatest { result ->
            when (result) {
                is Resources.Success -> {
                    _uiFormState.update { it.copy(barcode = result.data?.rawValue ?: "") }
                }

                is Resources.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }

                else -> {
                    return@collectLatest
                }
            }
        }
    }

    private fun createNewProduct() {
        if (validateInputs()) return
        _uiState.update { it.copy(isProductCreated = true) }
    }

    private fun validateInputs(): Boolean {
        val nameResult = validateNonEmptyFieldUseCase(_uiFormState.value.name)
        val barcodeResult = validateNonEmptyFieldUseCase(_uiFormState.value.barcode)
        val pricePerUnit = validateNonEmptyFieldUseCase(_uiFormState.value.pricePerUnit.toString())
        val datePurchase = validateNonEmptyFieldUseCase(_uiFormState.value.datePurchase)
        val minStockLevel = validateNonEmptyFieldUseCase(_uiFormState.value.minStockLevel)
        val category = validateNonEmptyFieldUseCase(_uiFormState.value.category?.name ?: "")

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
}

