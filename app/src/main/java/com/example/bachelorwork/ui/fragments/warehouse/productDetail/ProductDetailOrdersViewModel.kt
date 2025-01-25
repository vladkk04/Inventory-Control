package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.ui.model.product.detail.ProductDetailOrdersUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailOrdersViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases,
    private val navigator: Navigator,
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
        handleResult(orderUseCases.getOrders(),
            onSuccess = { orders ->
                /*val listOfProductDetailOrderItem = orders.flatMap { order ->
                    order.products.filter { it.id == productRouteArg.id }
                        .map { subItem ->
                            ProductDetailOrderItem(
                                order.id,
                                order.orderedAt,
                                subItem
                            )
                        }
                }
                _uiState.update { state ->
                    state.copy(
                        orders = listOfProductDetailOrderItem,
                        noOrders = listOfProductDetailOrderItem.isEmpty()
                    )
                }*/
            }
        )
    }

}