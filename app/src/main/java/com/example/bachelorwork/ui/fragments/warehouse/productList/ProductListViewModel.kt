package com.example.bachelorwork.ui.fragments.warehouse.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.ProductOrder
import com.example.bachelorwork.domain.model.product.ProductViewType
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.product.SortDirection
import com.example.bachelorwork.domain.model.product.toProductListItemUI
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val navigator: Navigator
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(ProductListUIState())

    val uiState = combine(
        _uiState.asStateFlow(),
        searchQuery
    ) { state, query ->
        if (query.isBlank()) {
            state
        } else {
            state.copy(
                products = state.products.filter { it.name.contains(query, ignoreCase = true) }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductListUIState())

    init {
        getProducts(_uiState.value.orderBy)
    }

    fun searchProducts(query: String) {
        this.searchQuery.value = query
    }

    fun changeViewType() {
        _uiState.update {
            it.copy(
                viewType = ProductViewType.entries[(it.viewType.ordinal + 1) % 2]
            )
        }
    }

    fun getProductsChangeSortBy(sortBy: SortBy) {
        when (sortBy) {
            SortBy.NAME -> {
                getProducts(
                    uiState.value.orderBy.copy(
                        sortBy = SortBy.NAME
                    )
                )
            }

            SortBy.PRICE -> {
                getProducts(
                    uiState.value.orderBy.copy(
                        sortBy = SortBy.PRICE
                    )
                )
            }
        }
    }

    fun getProductsChangeSortDirection() {
        getProducts(
            uiState.value.orderBy.copy(
                sortDirection = SortDirection.entries[(uiState.value.orderBy.sortDirection.ordinal + 1) % 2]
            )
        )
    }

    private fun getProducts(orderBy: ProductOrder) {
        val result = productUseCases.getProducts.getProducts(orderBy)
        handleResult(result, onSuccess = {
            _uiState.value = _uiState.value.copy(
                products = it.toProductListItemUI(),
                orderBy = orderBy
            )
        })
    }

    fun navigateToCreateProduct() = viewModelScope.launch {
        navigator.navigate(Destination.ProductManage()) {

        }
    }

    fun navigateToItemDetail(position: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(position)) {
            popUpTo<Destination.Warehouse> {
                //inclusive = false
            }

            //launchSingleTop = true
            //restoreState = true
        }
    }
}