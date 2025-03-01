package com.example.bachelorwork.ui.fragments.warehouse.productManage

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.model.validator.ValidatorInputFieldFactory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.ui.model.product.manage.ProductManageFormEvent
import com.example.bachelorwork.ui.model.product.manage.ProductManageFormState
import com.example.bachelorwork.ui.model.product.manage.ProductManageUIState
import com.example.bachelorwork.ui.permissions.PermissionController
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
import com.example.bachelorwork.domain.model.validator.validators.ValidatorDecimalFormat
import com.example.bachelorwork.domain.model.validator.validators.ValidatorNotEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseProductManageViewModel (
    private val permissionController: PermissionController,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductManageUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductManageFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    fun startScanBarcode() {
        handleResult(barcodeScannerUseCase(), onSuccess = { barcode ->
            _uiState.update { it.copy(barcode = barcode.displayValue.toString()) }
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
        val validatorInputFieldFactory = ValidatorInputFieldFactory(
            inputs = arrayOf(
                _uiFormState.value.name,
                _uiFormState.value.barcode,
                _uiFormState.value.quantity,
                _uiFormState.value.minStockLevel,
                _uiFormState.value.category.name
            ),
            validators = setOf(
                ValidatorNotEmpty,
                ValidatorDecimalFormat
            )
        )

        _uiFormState.update {
            it.copy(
                nameError = validatorInputFieldFactory.errorMessages[_uiFormState.value.name],
                barcodeError = validatorInputFieldFactory.errorMessages[_uiFormState.value.barcode],
                quantityError = validatorInputFieldFactory.errorMessages[_uiFormState.value.quantity],
                minStockLevelError = validatorInputFieldFactory.errorMessages[_uiFormState.value.minStockLevel],
                categoryError = validatorInputFieldFactory.errorMessages[_uiFormState.value.category.name]
            )
        }

        return validatorInputFieldFactory.hasError
    }
}