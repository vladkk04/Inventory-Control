package com.example.bachelorwork.ui.fragments.warehouse

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.ui.model.product.productManage.ProductManageFormEvent
import com.example.bachelorwork.ui.model.product.productManage.ProductManageFormState
import com.example.bachelorwork.ui.utils.extensions.handleResult
import com.example.bachelorwork.ui.utils.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.utils.snackbar.SnackbarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class BaseProductManageViewModel (
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val validatorNotEmptyUseCase: ValidatorNotEmptyUseCase,
) : ViewModel() {

    private val _uiFormState = MutableStateFlow(ProductManageFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    fun increaseQuantity() {
        _uiFormState.update { it.increaseQuantity() }
    }

    fun decreaseQuantity() {
        _uiFormState.update { it.decreaseQuantity() }
    }

    fun startScanBarcode() {
        handleResult(barcodeScannerUseCase(), onSuccess = { barcode ->
            _uiFormState.update { it.copy(barcode = barcode.displayValue.toString()) }
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })
    }

    fun onEvent(event: ProductManageFormEvent) {
        when (event) {
            is ProductManageFormEvent.NameChanged -> {
                _uiFormState.update { it.copy(name = event.name, nameError = null) }
            }

            is ProductManageFormEvent.BarcodeChanged -> {
                _uiFormState.update { it.copy(barcode = event.barcode, barcodeError = null) }
            }

            is ProductManageFormEvent.QuantityChanged -> {
                event.quantity.toIntOrNull()
                    ?.let { quantity -> _uiFormState.update { it.copy(quantity = quantity) } }
            }

            is ProductManageFormEvent.UnitChanged -> {
                _uiFormState.update { it.copy(productUnit = event.productUnit) }
            }

            is ProductManageFormEvent.CategoryChanged -> {
                _uiFormState.update {
                    it.copy(
                        category = event.category,
                        categoryError = null
                    )
                }
            }

            is ProductManageFormEvent.MinStockLevelChanged -> {
                _uiFormState.update {
                    it.copy(
                        minStockLevel = event.minStockLevel,
                        minStockLevelError = null
                    )
                }
            }

            is ProductManageFormEvent.TagsChanged -> {
                _uiFormState.update { it.copy(tags = event.tags) }
            }

            is ProductManageFormEvent.DescriptionChanged -> {
                _uiFormState.update { it.copy(description = event.description) }
            }
        }
    }

    protected fun validateInputs(): Boolean {
        val nameResult = validatorNotEmptyUseCase(_uiFormState.value.name)
        val barcodeResult = validatorNotEmptyUseCase(_uiFormState.value.barcode)
        val minStockLevel = validatorNotEmptyUseCase(_uiFormState.value.minStockLevel)
        val category = validatorNotEmptyUseCase(_uiFormState.value.category.name)

        val hasError = listOf(
            nameResult,
            barcodeResult,
            minStockLevel,
            category
        ).any { it.success }

        _uiFormState.update {
            it.copy(
                nameError = nameResult.errorMessage,
                barcodeError = barcodeResult.errorMessage,
                minStockLevelError = minStockLevel.errorMessage,
                categoryError = category.errorMessage
            )
        }

        return hasError
    }
}