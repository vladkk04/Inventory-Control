package com.example.bachelorwork.ui.fragments.orders.manage.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.order.OrderProductSelectedData
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.orders.manage.BaseOrderManageViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val orderUseCase: OrderUseCases,
    private val navigator: AppNavigator,
    val savedStateHandle: SavedStateHandle
) : BaseOrderManageViewModel(navigator) {

/*
    private val _uiState = MutableStateFlow(OrderCreateUiState())
    val uiStateForm = _uiState.asStateFlow()
*/


    fun createOrder() = viewModelScope.launch {
        sendSnackbarEvent(SnackbarEvent("No products added"))

        /*if (_uiState.value.addedProduct.isEmpty()) {
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
                discount = _uiState.value.discount,
                discountType = _uiState.value.discountType,
                total = _uiState.value.total
            ),
            *_uiState.value.addedProduct.toTypedArray()
        )

        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Order created"))
        }, onFailure = {
            sendSnackbarEvent(SnackbarEvent(it.message.toString()))
        })
        appNavigator.navigateUp()*/
    }

    fun addProductToOrder(data: OrderProductSelectedData) {
/*
        if (_uiState.value.addedProduct.any { it.id == data.productSelectedId }) {
            updateAddedProduct(data)
        }

        if (editProductId != null) {
            deleteAddedProduct(_uiState.value.addedProduct.find { it.id == editProductId }!!)
            editProductId = null
        }*/

       /* handleResult(
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
        )*/
    }

  /*  fun setDiscount(discount: Double?) {
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
        appNavigator.navigate(
            Destination.OrderManageDiscount(
                discount = _uiState.value.discount,
                discountType = discountType
            )
        )
    }

    fun navigateToOrderEditAddedProduct(product: OrderAddedProduct) = viewModelScope.launch {
        editProductId = product.id
        appNavigator.navigate(
            Destination.OrderEditAddedProduct(
                product.id,
                product.quantity,
                product.rate
            )
        )
    }

    fun navigateToOrderAddProduct() = viewModelScope.launch {
        appNavigator.navigate(Destination.OrderAddProduct)
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
    }*/
}
