package com.example.inventorycotrol.ui.fragments.orders.manage.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.order.Attachment
import com.example.inventorycotrol.domain.model.order.OrderAddedProduct
import com.example.inventorycotrol.domain.model.order.OrderDiscount
import com.example.inventorycotrol.domain.model.order.OrderProduct
import com.example.inventorycotrol.domain.model.order.OrderProductSelectedData
import com.example.inventorycotrol.domain.model.order.OrderRequest
import com.example.inventorycotrol.domain.model.updateStock.ProductStockUpdate
import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.order.OrderUseCases
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.model.order.manage.OrderManageUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import com.example.inventorycotrol.domain.model.file.FileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val updateStockUseCases: ProductUpdateStockUseCases,
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle,
    private val fileUseCases: FileUseCases,
    private val dataStoreManager: DataStoreManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderManageUiState())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            val currency =
                dataStoreManager.getPreference(AppConstants.ORGANISATION_CURRENCY).firstOrNull()

            _uiState.update {
                it.copy(
                    currency = AppConstants.CURRENCY_SYMBOLS[currency] ?: Currency.getInstance(
                        currency
                    ).symbol
                )
            }
        }
    }

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        navigator.navigate(Destination.OrderProductSelector(_uiState.value.currency))
    }

    fun navigateToOrderManageDiscount(discountType: DiscountType) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                discount = 0.00,
                discountType = discountType
            )
        }

        recalculateOrder()

        navigator.navigate(
            Destination.OrderManageDiscount(
                subtotal = _uiState.value.subtotal,
                discount = _uiState.value.discount,
                discountType = discountType
            )
        )
    }

    fun addProductToOrder(data: OrderProductSelectedData) = viewModelScope.launch {
        val existingProductIndex =
            _uiState.value.addedProduct.indexOfFirst { it.id == data.productSelectedId }

        when (val response = productUseCase.getProducts.getById(data.productSelectedId).first()) {
            Resource.Loading -> Unit
            is Resource.Error -> {
                _uiState.update { state ->
                    state.copy(addedProduct = state.addedProduct.filterNot { it.id == data.productSelectedId }
                        .toSet())
                }
                recalculateOrder()
            }

            is Resource.Success -> {
                _uiState.update { state ->
                    val updatedProduct = OrderAddedProduct(
                        id = response.data.id,
                        name = response.data.name,
                        unit = response.data.unit,
                        image = response.data.imageUrl,
                        previousStock = response.data.quantity,
                        price = data.rate,
                        quantity = data.quantity,
                        total = data.rate * data.quantity
                    )

                    val updatedList = if (existingProductIndex != -1) {
                        state.addedProduct.toMutableList().apply {
                            this[existingProductIndex] = updatedProduct
                        }
                    } else {
                        state.addedProduct + updatedProduct
                    }
                    state.copy(addedProduct = updatedList.toSet())
                }
                recalculateOrder()
            }
        }
}


fun deleteAddedProduct(item: OrderAddedProduct) {
    _uiState.update { state ->
        state.copy(
            addedProduct = state.addedProduct.filterNot { it.id == item.id }.toSet(),
            discount = if (state.discountType == DiscountType.FIXED) 0.00 else state.discount,
        )
    }
    recalculateOrder()
}

private fun recalculateOrder() {
    _uiState.update { state ->
        val subtotal = state.addedProduct.sumOf { it.total }

        val discountAmount = when (state.discountType) {
            DiscountType.PERCENTAGE -> subtotal * (state.discount / 100)
            DiscountType.FIXED -> state.discount
        }

        val total = (subtotal - discountAmount).coerceAtLeast(0.0)

        state.copy(
            subtotal = subtotal,
            total = total
        )
    }
}

fun setDiscount(discount: Double) {
    _uiState.update { state ->
        state.copy(
            discount = discount
        )
    }
    recalculateOrder()
}

fun changeComment(value: String) {
    _uiState.update { state ->
        state.copy(
            comment = value
        )
    }
}

fun addAttachments(attachments: List<FileData>) {
    _uiState.update { state ->
        state.copy(
            attachments = state.attachments + attachments
        )
    }
}

fun removeAttachment(attachment: FileData) {
    _uiState.update { state ->
        state.copy(
            attachments = state.attachments - attachment
        )
    }
}

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
