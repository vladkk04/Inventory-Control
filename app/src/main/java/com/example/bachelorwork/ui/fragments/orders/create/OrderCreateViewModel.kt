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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val navigator: Navigator,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderCreateUiState())
    val uiState = _uiState.asStateFlow()

    private var editProductId: Int? = null

    fun createOrder() = viewModelScope.launch {

        if (_uiState.value.addedProduct.isEmpty()) {
            sendSnackbarEvent(SnackbarEvent("No products added"))
            return@launch
        }

        if (_uiState.value.total < 0) {
            sendSnackbarEvent(SnackbarEvent("Total can not be negative"))
            return@launch
        }

        val result = orderUseCase.createOrder(
            OrderEntity(
                id = ((100000..999999).random().toString() + System.currentTimeMillis()
                    .toString()).take(7).toInt(),
                orderedAt = Calendar.getInstance().time,
                orderedBy = "chang later",
                total = _uiState.value.total
            )
        )

        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Order created"))
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })
        navigator.navigateUp()
    }

    fun addProductToOrder(data: OrderProductSelectedData) {

        if (editProductId != null) {
            deleteAddedProduct(_uiState.value.addedProduct.find { it.id == editProductId }!!)
            editProductId = null
        }

        if (_uiState.value.addedProduct.any { it.id == data.productSelectedId }) {
            updateAddedProduct(data)
            return
        }

        handleResult(
            productUseCase.getProducts.getProductById(data.productSelectedId),
            onSuccess = { product ->
                val subtotal =
                    _uiState.value.addedProduct.sumOf { it.total } + (data.rate * data.quantity)

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
                        subtotal = subtotal,
                        total = calculateTotal(
                            subtotal,
                            _uiState.value.discount,
                            _uiState.value.discountType
                        )
                    )
                }
            }
        )
    }

    fun setDiscount(discount: Double?) {
        if (discount == null) return
        _uiState.update { state ->
            state.copy(
                discount = discount,
                total = calculateTotal(
                    _uiState.value.subtotal,
                    discount,
                    _uiState.value.discountType
                )
            )
        }
    }

    fun deleteAddedProduct(product: OrderAddedProduct) {
        _uiState.update { state ->
            state.copy(
                addedProduct = state.addedProduct.filter { product != it }.toSet(),
                subtotal = state.addedProduct.sumOf { it.total } - product.total,
                total = calculateTotal(
                    state.addedProduct.sumOf { it.total } - product.total,
                    _uiState.value.discount,
                    _uiState.value.discountType
                )
            )
        }
    }

    fun navigateToOrderSetDiscount(discountType: DiscountType) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                discount = 0.00,
                discountType = discountType,
                total = calculateTotal(state.subtotal, state.discount, discountType)
            )
        }
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
                }.toSet(),
                subtotal = state.addedProduct.sumOf { it.total } + (data.rate * data.quantity)
            )
        }
    }

    private fun calculateTotal(
        subTotal: Double,
        discount: Double,
        discountType: DiscountType
    ): Double {
        return if (discountType == DiscountType.PERCENTAGE) {
            subTotal - (subTotal * (discount / 100))
        } else {
            subTotal - discount
        }
    }
}
