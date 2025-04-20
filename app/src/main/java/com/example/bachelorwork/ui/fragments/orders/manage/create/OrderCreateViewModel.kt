package com.example.bachelorwork.ui.fragments.orders.manage.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.domain.model.order.Attachment
import com.example.bachelorwork.domain.model.order.OrderDiscount
import com.example.bachelorwork.domain.model.order.OrderProduct
import com.example.bachelorwork.domain.model.order.OrderRequest
import com.example.bachelorwork.domain.model.updateStock.ProductStockUpdate
import com.example.bachelorwork.domain.model.updateStock.ProductUpdateStockRequest
import com.example.bachelorwork.domain.usecase.file.FileUseCases
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.bachelorwork.ui.fragments.orders.manage.BaseOrderManageViewModel
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val updateStockUseCases: ProductUpdateStockUseCases,
    private val navigator: AppNavigator,
    val savedStateHandle: SavedStateHandle,
    private val fileUseCases: FileUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseOrderManageViewModel(productUseCase, navigator) {

    fun createOrder() = viewModelScope.launch {

        val uploadedAttachments = uiState.value.attachments.mapNotNull { attachment ->
            attachment.uri?.let { uri ->
                try {
                    val fileUrl = fileUseCases.uploadFileUseCase(
                        FolderType.ORDER_ATTACHMENTS,
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
                }

                is Resource.Success -> {
                    updateStock()
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)
    }

    private fun updateStock() = viewModelScope.launch {
        updateStockUseCases.updateStock.invoke(
            ProductUpdateStockRequest(
                uiState.value.addedProduct.map {
                    ProductStockUpdate(
                        it.id,
                        it.previousStock,
                        it.quantity
                    )
                },
                stockOperationType = StockOperationType.STOCK_IN
            )
        ).onEach { result ->
            when (result) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {

                }

                is ApiResponseResult.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Order created"))
                    navigator.navigateUp()
                }
            }
        }.launchIn(viewModelScope)
    }
}
