package com.example.bachelorwork.ui.fragments.warehouse.productDetail.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.repository.ProductRepository
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductDetailOrdersUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailOrdersViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases,
    private val navigator: AppNavigator,
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productRouteArg = Destination.from<Destination.ProductDetail>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProductDetailOrdersUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getOrdersContainingProduct()
    }

    fun navigateToOrder(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.OrderDetail(id))
    }

    private fun getOrdersContainingProduct() {
        /*handleResult(orderUseCases.getOrders.getOrdersContainingProductById(productRouteArg.id),
            onSuccess = { orders ->
                orders.forEach { order ->
                    val result =
                        productRepository.getProductPojoById(order.productCrossRef.productId)
                            .map { runCatching { it } }
                    handleResult(result, onSuccess = { product ->
                        _uiState.update { state ->
                            state.copy(
                                orders = state.orders + ProductDetailOrderUi(
                                    orderId = order.order.id,
                                    orderedAt = order.order.orderedAt,
                                    name = product.product.name,
                                    quantity = order.productCrossRef.orderRate,
                                    unit = product.product.unit.name,
                                    price = order.productCrossRef.orderQuantity
                                )
                            )
                        }
                    })
                }
            }
        )*/
    }

}