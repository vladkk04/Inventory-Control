package com.example.bachelorwork.ui.fragments.warehouse.productManage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.toEntity
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.inputFieldValidators.ValidatorNotEmptyUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.model.productManage.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.productManage.ProductCreateFormState
import com.example.bachelorwork.ui.model.productManage.ProductManageUIState
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
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProductManageViewModel @Inject constructor(
    private val startBarcodeScannerUseCase: BarcodeScannerUseCase,
    private val validatorNotEmptyUseCase: ValidatorNotEmptyUseCase,
    private val productUseCase: ProductUseCases,
    private val categoryUseCase: ProductCategoryUseCases,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val productManageRoute = Destination.from<Destination.ProductManage>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductManageUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProductCreateFormState())
    val uiFormState get() = _uiFormState.asStateFlow()

    init {
        fetchProduct()
        getCategories()
    }

    private fun getCategories() {
        handleResult(categoryUseCase.getCategories(), onSuccess = {
            _uiState.update { state -> state.copy(categories = it) }
        })
    }

    private fun fetchProduct() {
        val productId = productManageRoute.id
        if (productId != null) {
            val result = productUseCase.getProducts.getProductById(productId)
            handleResult(result, onSuccess = {
                _uiState.update { state ->
                    state.copy(
                        titleToolbar = "Edit Product",
                        product = it
                    )
                }
            })
        } else {
            _uiState.update { state ->
                state.copy(
                    titleToolbar = "Create Product"
                )
            }
        }
    }

    private suspend fun createProduct() =
        productUseCase.createProduct(
            ProductEntity(
                categoryId = uiFormState.value.category.id,
                name = uiFormState.value.name,
                barcode = uiFormState.value.barcode,
                quantity = uiFormState.value.quantity,
                price = uiFormState.value.pricePerUnit.toDouble(),
                productUnit = uiFormState.value.productUnit,
                totalPrice = uiFormState.value.totalPrice,
                datePurchase = Date(),
                minStockLevel = uiFormState.value.minStockLevel.toInt(),
                tags = uiFormState.value.tags,
            )
        )


    private suspend fun updateProduct(id: Int) =
        productUseCase.updateProduct(
            ProductEntity(
                id = id,
                categoryId = uiFormState.value.category.id,
                name = uiFormState.value.name,
                barcode = uiFormState.value.barcode,
                quantity = uiFormState.value.quantity,
                price = uiFormState.value.pricePerUnit.toDouble(),
                productUnit = uiFormState.value.productUnit,
                totalPrice = uiFormState.value.totalPrice,
                datePurchase = Date(),
                minStockLevel = uiFormState.value.minStockLevel.toInt(),
                tags = uiFormState.value.tags,
            )
        )


    fun modifyProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        val productId = productManageRoute.id

        val result = if (productId != null) {
            updateProduct(productId)
        } else {
            createProduct()
        }

        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Product saved successfully"))
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
        handleResult(startBarcodeScannerUseCase(), onSuccess = { barcode ->
            _uiState.update { it.copy(product = it.product?.copy(barcode = barcode.displayValue.toString())) }
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })
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
}

