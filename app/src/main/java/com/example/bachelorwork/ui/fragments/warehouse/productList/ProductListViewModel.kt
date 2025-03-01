package com.example.bachelorwork.ui.fragments.warehouse.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.ProductViewDisplayMode
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.product.list.ProductListUIState
import com.example.bachelorwork.ui.model.product.list.ProductSearchUIState
import com.example.bachelorwork.ui.model.product.list.sortBy
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases, private val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _searchUiState = MutableStateFlow(ProductSearchUIState())
    val searchUiState get() = _searchUiState.asStateFlow()

    init { getProducts() }

    fun searchProducts(query: String) {
        if (query.isEmpty()) {
            _searchUiState.update { state ->
                state.copy(products = emptyList())
            }
            return
        }

        val filteredProducts = uiState.value.products.filter {
            it.name.contains(query, ignoreCase = true)
        }

        _searchUiState.update { state ->
            state.copy(products = filteredProducts)
        }
    }

    fun changeProductViewDisplayType() {
        _uiState.update {
            it.copy(
                viewDisplayType = ProductViewDisplayMode.entries[(it.viewDisplayType.ordinal + 1) % 2]
            )
        }
    }

    fun changeSortDirection() {
        _uiState.update { state ->
            val newSortDirection = state.sortOptions.sortDirection.opposite()
            val newSortOptions = state.sortOptions.copy(sortDirection = newSortDirection)

            state.copy(
                products = state.products.sortBy(sortOptions = newSortOptions),
                sortOptions = newSortOptions
            )
        }
    }

    fun getProductsSortedBy(sortBy: SortBy) {
        _uiState.update { state ->

            val newSortOptions = state.sortOptions.copy(sortBy = sortBy)

            state.copy(
                products = state.products.sortBy(newSortOptions),
                sortOptions = newSortOptions
            )
        }
    }

    fun navigateToCreateItem() = viewModelScope.launch {
        navigator.navigate(Destination.CreateProduct)
    }

    fun navigateToFilters() = viewModelScope.launch {
        navigator.navigate(Destination.WarehouseFilters)
    }

    fun openNavigationDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    fun navigateToItemDetail(id: Int) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(id))
    }

    private fun getProducts() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        /*val result = productUseCases.getProducts.getProducts(_uiState.value.sortOptions)
        handleResult(result, onSuccess = { products ->
            _uiState.update { state ->
                state.copy(
                    products = products.map { it.toProductUi() },
                    isLoading = false
                )
            }
        }, onFailure = { e ->
            sendSnackbarEvent(SnackbarEvent(e.message.toString()))
        })*/
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(filtersCount = 0)
        }
    }
}