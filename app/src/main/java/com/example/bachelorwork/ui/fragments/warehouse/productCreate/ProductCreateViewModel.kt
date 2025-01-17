package com.example.bachelorwork.ui.fragments.warehouse.productCreate

import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.BaseProductManageViewModel
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.snackbar.SnackbarAction
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
) : BaseProductManageViewModel(barcodeScannerUseCase) {

    private suspend fun createProductRoom() =
        productUseCase.createProduct(
            ProductEntity(
                categoryId = uiFormState.value.category.id,
                name = uiFormState.value.name,
                barcode = uiFormState.value.barcode,
                quantity = uiFormState.value.quantity,
                productUnit = uiFormState.value.productUnit,
                minStockLevel = uiFormState.value.minStockLevel.toInt(),
                tags = uiFormState.value.tags,
                description = uiFormState.value.description,
                timelineHistory = listOf(
                    ProductTimelineHistory.ProductTimelineCreate(
                        createdAt = Calendar.getInstance().time,
                        createdBy = "Vlad"
                    )
                )
            )
        )

    fun createProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        val result = createProductRoom()

        handleResult(result, onSuccess = {
            sendSnackbarEvent(
                event = SnackbarEvent(
                    "Product created successfully",
                    action = SnackbarAction("show") {

                    }
                )
            )
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })

        navigator.navigateUp()
    }
}

