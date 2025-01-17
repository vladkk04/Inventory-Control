package com.example.bachelorwork.ui.fragments.orders.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.domain.model.order.OrderProductSelectedData
import com.example.bachelorwork.domain.model.order.toOrderSubItem
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.order.create.DiscountType
import com.example.bachelorwork.ui.model.order.create.OrderCreateUiState
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
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val navigator: Navigator,
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderCreateUiState())
    val uiState = _uiState.asStateFlow()

    private var editProductId: Int? = null

    private suspend fun createOrderRoom() =
        orderUseCase.createOrder(
            OrderEntity(
                total = _uiState.value.total,
                products = _uiState.value.addedProduct.map { it.toOrderSubItem() }.toList(),
            )
        )


    fun createOrder() = viewModelScope.launch {
        val result = createOrderRoom()

        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Order created"))
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })

        navigator.navigateUp()
    }

    fun addProductToOrder(data: OrderProductSelectedData) {
        if (_uiState.value.addedProduct.any { it.id == data.productSelectedId }) {
            updateAddedProduct(data)
            return
        }
        
        if (editProductId != null) {
            deleteAddedProduct(_uiState.value.addedProduct.find { it.id == editProductId }!!)
            editProductId = null
        }

        handleResult(
            productUseCase.getProducts.getProductById(data.productSelectedId),
            onSuccess = { product ->
                _uiState.update { state ->
                    state.copy(
                        addedProduct = state.addedProduct + OrderAddedProduct(
                            id = product.id,
                            name = product.name,
                            image = product.image,
                            unit = product.unit,
                            quantity = data.quantity,
                            rate = data.rate,
                            total = (data.rate * data.quantity),
                        ),
                        subtotal = state.subtotal + (data.rate * data.quantity),
                        total = state.total + (data.rate * data.quantity) - state.discount
                    )
                }
            }
        )
    }

    fun setDiscount(discount: Double) {
        _uiState.update { state ->
            state.copy(
                discount = discount,
                total = if (state.discountType == DiscountType.PERCENTAGE) {
                    state.subtotal - (state.subtotal * (discount / 100))
                } else {
                    state.subtotal - discount
                }
            )
        }
    }

    fun deleteAddedProduct(product: OrderAddedProduct) {
        _uiState.update { state ->
            state.copy(
                addedProduct = state.addedProduct.filter { product != it }.toSet(),
                subtotal = state.subtotal - product.total,
                total = state.total - product.total + (product.rate * product.quantity) - state.discount
            )
        }
    }

    fun navigateToOrderSetDiscount(discountType: DiscountType) = viewModelScope.launch {
        navigator.navigate(
            Destination.OrderManageDiscount(
                discount = _uiState.value.discount,
                discountType = discountType
            )
        )
    }

    fun navigateToOrderEditAddedProduct(product: OrderAddedProduct) = viewModelScope.launch {
        editProductId = product.id
        navigator.navigate(
            Destination.OrderEditAddedProduct(
                product.id,
                product.quantity,
                product.rate
            )
        )
    }

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        navigator.navigate(Destination.OrderAddProduct)
    }

    private fun updateAddedProduct(data: OrderProductSelectedData) {
        _uiState.update { state ->
            state.copy(
                addedProduct = state.addedProduct.map { product ->
                    if (product.id == data.productSelectedId) {
                        product.copy(
                            quantity = data.quantity,
                            rate = data.rate,
                            total = data.rate * data.quantity
                        )
                    } else {
                        product
                    }
                }.toSet()
            )
        }
    }
}
