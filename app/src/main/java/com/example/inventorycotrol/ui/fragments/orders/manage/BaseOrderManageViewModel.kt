package com.example.inventorycotrol.ui.fragments.orders.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.order.OrderAddedProduct
import com.example.inventorycotrol.domain.model.order.OrderProductSelectedData
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.model.order.manage.OrderManageUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.utils.FileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency


abstract class BaseOrderManageViewModel(
    private val productsUseCase: ProductUseCases,
    private val navigator: AppNavigator,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _uiState = MutableStateFlow(OrderManageUiState())
    val uiState = _uiState.asStateFlow()

    protected val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            val currency = dataStoreManager.getPreference(AppConstants.ORGANISATION_CURRENCY).firstOrNull()

           _uiState.update {
               it.copy(
                   currency = AppConstants.CURRENCY_SYMBOLS[currency] ?: Currency.getInstance(currency).symbol
               )
           }
        }
    }

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        navigator.navigate(Destination.OrderAddProduct(_uiState.value.currency))
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
        val existingProductIndex = _uiState.value.addedProduct.indexOfFirst { it.id == data.productSelectedId }

        productsUseCase.getProducts.getById(data.productSelectedId).collect { response ->
            when (response) {
                Resource.Loading -> Unit
                is Resource.Error -> Unit
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
    }



    fun deleteAddedProduct(item: OrderAddedProduct) {
        _uiState.update { state ->
            state.copy(
                addedProduct = state.addedProduct.filterNot { it.id == item.id }.toSet(),
                discount = if(state.discountType == DiscountType.FIXED) 0.00 else state.discount,
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

}