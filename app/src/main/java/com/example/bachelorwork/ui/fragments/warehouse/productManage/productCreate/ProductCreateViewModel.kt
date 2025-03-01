package com.example.bachelorwork.ui.fragments.warehouse.productManage.productCreate

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.productManage.BaseProductManageViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.permissions.PermissionController
import com.example.bachelorwork.ui.snackbar.SnackbarAction
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    private val productUseCase: ProductUseCases,
    private val navigator: AppNavigator,
    private val permissionUseCase: PermissionController,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseProductManageViewModel(permissionUseCase, barcodeScannerUseCase) {

    private suspend fun createProductEntity() = productUseCase.createProduct(
        ProductEntity(
            categoryId = uiFormState.value.category.id,
            name = uiFormState.value.name.replaceFirstChar(Char::uppercase),
            barcode = uiFormState.value.barcode,
            quantity = uiFormState.value.quantity.toDouble(),
            unit = uiFormState.value.productUnit,
            minStockLevel = uiFormState.value.minStockLevel.toDouble(),
            tags = uiFormState.value.tags,
            description = uiFormState.value.description,
            timelineHistory = listOf(
                ProductTimelineHistory.ProductTimelineCreate(
                    createdAt = Calendar.getInstance().time, createdBy = "Vlad"
                )
            )
        )
    )
    fun createProduct() = viewModelScope.launch(defaultDispatcher) {
        if (validateInputs()) return@launch

        handleResult(createProductEntity(),
            onSuccess = {
                sendSnackbarEvent(
                    event = SnackbarEvent("Product created successfully",
                        action = SnackbarAction("show") {

                        }
                    )
                )
                launch { navigator.navigateUp() }
            },
            onFailure = {
                val errorMessage = when (it) {
                    is SQLiteConstraintException -> {
                        "Product with this name already exists"
                    }
                    else -> { "Something went wrong" }
                }
                sendSnackbarEvent(SnackbarEvent(errorMessage))
            }
        )
    }
}

