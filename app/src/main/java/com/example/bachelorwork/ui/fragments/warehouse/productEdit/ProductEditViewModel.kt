package com.example.bachelorwork.ui.fragments.warehouse.productEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.domain.model.order.toOrderEntity
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.BaseProductManageViewModel
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val navigator: Navigator,
    barcodeScannerUseCase: BarcodeScannerUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseProductManageViewModel(barcodeScannerUseCase) {

    private val productEditRoute = Destination.from<Destination.EditProduct>(savedStateHandle)

    private val _product = MutableStateFlow<Product?>(null)
    val product get() = _product.asStateFlow()

    init { getProduct() }

    private fun getProduct() {
        val result = productUseCase.getProducts.getProductById(productEditRoute.id)

        handleResult(result, onSuccess = { product ->
            _product.update { product }
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
                description = uiFormState.value.description,
                timelineHistory = product.value!!.timelineHistory +
                        ProductTimelineHistory.ProductTimelineUpdate(
                            updatedAt = Calendar.getInstance().time,
                            updatedBy = "Vladyslav Klymiuk"
                        )
            )
        )

    fun updateProduct() = viewModelScope.launch {
        if (validateInputs()) return@launch

        val result = updateProductRoom(productEditRoute.id)

        handleResult(orderUseCase.getOrders(), onSuccess = { orders ->
            val updatedOrders = orders.flatMap { it.items }
                .map { it.copy(unit = uiFormState.value.productUnit.name) }

            val orderEntity = orders.map {
                it.copy(items = updatedOrders).toOrderEntity()
            }
            viewModelScope.launch {
                handleResult(orderUseCase.updateOrder(*orderEntity.toTypedArray()))
            }
        })

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

