package com.example.bachelorwork.ui.fragments.warehouse.productManage.productEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.file.FileUseCases
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.productManage.BaseProductManageViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.FileMimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val navigator: AppNavigator,
    barcodeScannerUseCase: BarcodeScannerUseCase,
    savedStateHandle: SavedStateHandle,
    private val fileUseCases: FileUseCases
) : BaseProductManageViewModel(barcodeScannerUseCase) {

    private val productEditRoute = Destination.from<Destination.EditProduct>(savedStateHandle)

    private val _product = MutableStateFlow<Product?>(null)
    val product get() = _product.asStateFlow()

    init { getProduct() }

    private fun getProduct() = viewModelScope.launch {
        productUseCase.getProducts.getById(productEditRoute.id).onEach { response ->
            when (response) {
                Resource.Loading -> Unit
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _product.update { response.data }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun updateProduct() = viewModelScope.launch {
        if (!isValidateInputs()) return@launch

        val url = image.value?.let {
            fileUseCases.uploadFileUseCase(FolderType.PRODUCTS, it, FileMimeType.PNG)
        }?.first()

        val updatedProduct = product.value?.copy(
            imageUrl = url,
            name = uiFormState.value.name,
            barcode = uiFormState.value.barcode,
            categoryName = uiFormState.value.categoryId,
            minStockLevel = uiFormState.value.minStockLevel.toDouble(),
            description = uiFormState.value.description,
            tags = uiFormState.value.tags,
        )

        updatedProduct?.let {
           productUseCase.updateProduct(it).onEach { response ->
                when (response) {
                    Resource.Loading -> Unit
                    is Resource.Error -> sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    is Resource.Success -> {
                        sendSnackbarEvent(SnackbarEvent("Product updated successfully"))
                        navigator.navigateUp()
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
        }

        sendSnackbarEvent(SnackbarEvent("Something went wrong"))

    }
}

