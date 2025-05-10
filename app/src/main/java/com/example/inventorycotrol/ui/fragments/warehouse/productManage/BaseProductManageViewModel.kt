package com.example.inventorycotrol.ui.fragments.warehouse.productManage

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.inventorycotrol.ui.model.product.manage.ProductManageFormEvent
import com.example.inventorycotrol.ui.model.product.manage.ProductManageFormState
import com.example.inventorycotrol.ui.model.product.manage.ProductManageUIState
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import com.example.inventorycotrol.ui.utils.extensions.handleResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseProductManageViewModel(
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
) : ViewModel() {

    protected val _uiState = MutableStateFlow(ProductManageUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductManageFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    private val _barcode = MutableStateFlow("")
    val barcode get() = _barcode.asStateFlow()

    val image = MutableStateFlow<Uri?> (null)

    fun startScanBarcode() {
        handleResult(barcodeScannerUseCase(), onSuccess = { barcode ->
            _barcode.update { barcode.displayValue.toString() }
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
                _uiFormState.update { it.copy(quantity = event.quantity, quantityError = null) }
            }

            is ProductManageFormEvent.UnitChanged -> {
                _uiFormState.update { it.copy(productUnit = event.productUnit) }
            }

            is ProductManageFormEvent.CategoryChanged -> {
                _uiFormState.update {
                    it.copy(categoryId = event.categoryId, categoryError = null)
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

    fun setupImage(file: Uri?) {
        image.update { file }
    }

    protected fun isValidateInputs(): Boolean {

        val inputValidator = InputValidator
            .create()
            .withNotEmpty()
            .build()

        val nameError = inputValidator.invoke(_uiFormState.value.name)
        val barcodeError = inputValidator.invoke(_uiFormState.value.barcode)
        val quantityError = inputValidator.invoke(_uiFormState.value.quantity)
        val minStockLevelError = inputValidator.invoke(_uiFormState.value.minStockLevel)
        val categoryError = inputValidator.invoke(_uiFormState.value.categoryId)

        _uiFormState.update {
            it.copy(
                nameError = nameError.errorMessage,
                barcodeError = barcodeError.errorMessage,
                quantityError = quantityError.errorMessage,
                minStockLevelError = minStockLevelError.errorMessage,
                categoryError = categoryError.errorMessage
            )
        }

        val hasError = (nameError.hasError || barcodeError.hasError || quantityError.hasError || minStockLevelError.hasError || categoryError.hasError)
        
        return !hasError
    }
}