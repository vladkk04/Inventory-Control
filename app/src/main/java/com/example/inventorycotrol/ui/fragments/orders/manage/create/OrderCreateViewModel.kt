package com.example.inventorycotrol.ui.fragments.orders.manage.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.order.Attachment
import com.example.inventorycotrol.domain.model.order.OrderDiscount
import com.example.inventorycotrol.domain.model.order.OrderProduct
import com.example.inventorycotrol.domain.model.order.OrderRequest
import com.example.inventorycotrol.domain.model.updateStock.ProductStockUpdate
import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.order.OrderUseCases
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.ui.fragments.orders.manage.BaseOrderManageViewModel
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val updateStockUseCases: ProductUpdateStockUseCases,
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle,
    private val fileUseCases: FileUseCases,
    dataStoreManager: DataStoreManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseOrderManageViewModel(productUseCase, navigator, dataStoreManager) {

    fun createOrder() = viewModelScope.launch {
        _isLoading.update { true }
        val uploadedAttachments = uiState.value.attachments.mapNotNull { attachment ->
            attachment.uri?.let { uri ->
                try {
                    val fileUrl = fileUseCases.uploadFileUseCase(
                        uri,
                        attachment.mimeType
                    ).first()

                    fileUrl?.let {
                        Attachment(
                            fileUrl,
                            attachment.size ?: ""
                        )
                    }
                } catch (e: Exception) {
                    sendSnackbarEvent(SnackbarEvent("Failed to upload files"))
                    return@launch
                }
            }
        }

        val newOrder = OrderRequest(
            uiState.value.addedProduct.map {
                OrderProduct(
                    productId = it.id,
                    quantity = it.quantity,
                    price = it.price
                )
            },
            OrderDiscount(
                uiState.value.discount,
                uiState.value.discountType
            ),
            uiState.value.comment,
            attachments = uploadedAttachments
        )

        orderUseCase.createOrder(newOrder).onEach { response ->
            when (response) {
                Resource.Loading -> {

                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    _isLoading.update { false }
                }

                is Resource.Success -> {
                    updateStock(response.data.id)
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)
    }

    private fun updateStock(orderId: String) = viewModelScope.launch {
        updateStockUseCases.updateStock.invoke(
            ProductUpdateStockRequest(
                uiState.value.addedProduct.map {
                    ProductStockUpdate(
                        it.id,
                        it.previousStock,
                        it.quantity
                    )
                },
                orderId
            )
        ).onEach { result ->
            when (result) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    _isLoading.update { false }
                    sendSnackbarEvent(SnackbarEvent("Order created"))
                    navigator.navigateUp()
                }
            }
        }.launchIn(viewModelScope)
    }
}
