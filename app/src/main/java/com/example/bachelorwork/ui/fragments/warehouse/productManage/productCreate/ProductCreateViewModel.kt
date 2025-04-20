package com.example.bachelorwork.ui.fragments.warehouse.productManage.productCreate

import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.domain.model.product.ProductRequest
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.file.FileUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.productManage.BaseProductManageViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.FileMimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val productUseCase: ProductUseCases,
    private val navigator: AppNavigator,
    private val fileUseCases: FileUseCases,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseProductManageViewModel(barcodeScannerUseCase) {

    fun createProduct() = viewModelScope.launch {

        val url = image.value?.let {
            fileUseCases.uploadFileUseCase(FolderType.PRODUCTS, it, FileMimeType.PNG)
        }?.first()

        if (!isValidateInputs()) return@launch

        val newProduct = ProductRequest(
            imageUrl = url,
            name = uiFormState.value.name,
            barcode = uiFormState.value.barcode,
            quantity = uiFormState.value.quantity.toDouble(),
            unit = uiFormState.value.productUnit,
            categoryId = uiFormState.value.categoryId,
            minStockLevel = uiFormState.value.minStockLevel.toDouble(),
            description = uiFormState.value.description,
            tags = uiFormState.value.tags
        )

        productUseCase.createProduct.invoke(newProduct).collectLatest { result ->
            when (result) {
                Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Product created"))
                    _uiState.update {
                        it.copy(
                            isManaged = true,
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}

