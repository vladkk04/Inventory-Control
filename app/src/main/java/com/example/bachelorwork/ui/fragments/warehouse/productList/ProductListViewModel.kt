package com.example.bachelorwork.ui.fragments.warehouse.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.SortDirection
import com.example.bachelorwork.domain.model.product.ProductDisplayMode
import com.example.bachelorwork.domain.model.product.ProductSortOptions
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.product.toProductUI
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import com.example.bachelorwork.ui.model.productList.ProductSearchUIState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _searchUiState = MutableStateFlow(ProductSearchUIState())
    val searchUiState get() = _searchUiState.asStateFlow()

    init {
        getProducts(_uiState.value.orderBy)
    }

    fun searchProducts(query: String) {
        if (query.isEmpty()) {
            _searchUiState.update { state ->
                state.copy(
                    products = emptyList(),
                    isNoItemsFound = false
                )
            }
            return
        }

        val filteredProducts = uiState.value.products.filter {
            it.name.contains(query, ignoreCase = true)
        }

        _searchUiState.update { state ->
            state.copy(
                products = filteredProducts,
                isNoItemsFound = filteredProducts.isEmpty()
            )
        }
    }

    fun changeViewType() {
        _uiState.update {
            it.copy(
                viewType = ProductDisplayMode.entries[(it.viewType.ordinal + 1) % 2]
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
        }
    }

    fun getProductsChangeSortDirection() {
        getProducts(
            uiState.value.orderBy.copy(
                sortDirection = SortDirection.entries[(uiState.value.orderBy.sortDirection.ordinal + 1) % 2]
            )
        )
    }

    private fun getProducts(orderBy: ProductSortOptions) {
        val result = productUseCases.getProducts.getProducts(orderBy)
        handleResult(result, onSuccess = {
            _uiState.value = _uiState.value.copy(
                products = it.toProductUI(),
                orderBy = orderBy,
                isNoProducts = it.isEmpty()
            )
        })
    }

    fun navigateToCreateItem() = viewModelScope.launch {
        navigator.navigate(Destination.CreateProduct) {
            launchSingleTop = true
        }
    }

    fun openNavigationDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    fun navigateToItemDetail(position: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(position)) {
            popUpTo<Destination.Warehouse>()
        }
    }
}