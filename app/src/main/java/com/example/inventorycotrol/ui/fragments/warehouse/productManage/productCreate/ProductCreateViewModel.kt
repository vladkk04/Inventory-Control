package com.example.inventorycotrol.ui.fragments.warehouse.productManage.productCreate

import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.model.product.ProductRequest
import com.example.inventorycotrol.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.fragments.warehouse.productManage.BaseProductManageViewModel
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import com.example.inventorycotrol.domain.model.file.FileMimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
        if (!isValidateInputs()) {
            return@launch
        }

        _uiState.update { it.copy(isLoading = true) }

        val url = image.value?.let {
            fileUseCases.uploadFileUseCase(it, FileMimeType.PNG)
        }?.first()

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

        productUseCase.createProduct.invoke(newProduct).distinctUntilChanged().collectLatest { result ->
            when (result) {
                Resource.Loading -> {
                }
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

