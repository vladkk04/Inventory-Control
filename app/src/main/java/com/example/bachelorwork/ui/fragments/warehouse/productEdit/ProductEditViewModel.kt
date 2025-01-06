package com.example.bachelorwork.ui.fragments.warehouse.productEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.BaseProductManageViewModel
import com.example.bachelorwork.ui.model.product.productManage.ProductEditUIState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import com.example.bachelorwork.ui.utils.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    barcodeScannerUseCase: BarcodeScannerUseCase,
    validatorNotEmptyUseCase: ValidatorNotEmptyUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseProductManageViewModel(barcodeScannerUseCase, validatorNotEmptyUseCase) {

    private val productEditRoute = Destination.from<Destination.EditProduct>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductEditUIState())
    val uiState get() = _uiState.asStateFlow()

    init { getProduct() }

    private fun getProduct() {
        val result = productUseCase.getProducts.getProductById(productEditRoute.id)

        handleResult(result, onSuccess = { product ->
            _uiState.update { it.copy(product = product) }
        }, onFailure = { e ->
            _uiState.update { it.copy(errorMessage = e.message) }
        })
    }

    private suspend fun updateProductRoom(id: Int) =
        productUseCase.updateProduct(
            ProductEntity(
                id = id,
                categoryId = uiFormState.value.category.id,
                name = uiFormState.value.name,
                barcode = uiFormState.value.barcode,
                quantity = uiFormState.value.quantity,
                productUnit = uiFormState.value.productUnit,
                minStockLevel = uiFormState.value.minStockLevel.toInt(),
                tags = uiFormState.value.tags,
            )
        )

    fun updateProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        val result = updateProductRoom(productEditRoute.id)

        handleResult(result, onSuccess = {
            sendSnackbarEvent(
                SnackbarEvent(message = "Product updated successfully")
            )
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })

        navigator.navigateUp()
    }
}

