package com.example.bachelorwork.ui.fragments.warehouse.productEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.toEntity
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.model.productDetail.ProductDetailUIState
import com.example.bachelorwork.ui.model.productManage.ProductManageFormEvent
import com.example.bachelorwork.ui.model.productManage.ProductManageFormState
import com.example.bachelorwork.ui.model.productManage.ProductEditUIState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import com.example.bachelorwork.ui.utils.snackbar.SnackbarAction
import com.example.bachelorwork.ui.utils.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val validatorNotEmptyUseCase: ValidatorNotEmptyUseCase,
    private val productUseCase: ProductUseCases,
    private val categoryUseCase: ProductCategoryUseCases,
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productEditRoute = Destination.from<Destination.EditProduct>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductEditUIState())
    private val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductManageFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    init {
        getProduct()
    }

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
                SnackbarEvent(
                    message = "Product updated successfully",
                    action = SnackbarAction("show") {

                    })
            )
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })

        navigator.navigateUp()
    }

    fun createCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.createCategory(category.toEntity())
        handleResult(result)
    }

    fun updateCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.updateCategory(category.toEntity())
        handleResult(result)
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.deleteCategory(category.toEntity())
        handleResult(result)
    }

    fun increaseQuantity() {
        _uiFormState.update { it.increaseQuantity() }
    }

    fun decreaseQuantity() {
        _uiFormState.update { it.decreaseQuantity() }
    }

    fun startScanBarcode() {
        handleResult(barcodeScannerUseCase(), onSuccess = { barcode ->
            _uiState.update { it.copy(product = it.product?.copy(barcode = barcode.displayValue.toString())) }
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

    private fun validateInputs(): Boolean {
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

