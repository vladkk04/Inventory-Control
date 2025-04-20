package com.example.bachelorwork.ui.fragments.warehouse.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.model.product.ProductViewDisplayMode
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.sorting.SortDirection
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.filters.SharedWarehouseFilterUiState
import com.example.bachelorwork.ui.model.product.list.ProductListUIState
import com.example.bachelorwork.ui.model.product.list.ProductSearchUIState
import com.example.bachelorwork.ui.model.product.list.moreFilters
import com.example.bachelorwork.ui.model.product.list.sortBy
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _searchUiState = MutableStateFlow(ProductSearchUIState())
    val searchUiState get() = _searchUiState.asStateFlow()

    private var currentFilters: SharedWarehouseFilterUiState = SharedWarehouseFilterUiState()


    init {
        getProducts()
    }

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
                products = state.products.sortBy(newSortOptions),
                sortOptions = newSortOptions
            )
        }
    }

    fun getProductsSortedBy(sortBy: SortBy) {
        _uiState.update { state ->
            val newSortOptions = if (state.sortOptions.sortBy == sortBy) {
                state.sortOptions.copy(sortDirection = state.sortOptions.sortDirection.opposite())
            } else {
                state.sortOptions.copy(sortBy = sortBy, sortDirection = SortDirection.ASCENDING)
            }

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

    fun navigateToItemDetail(id: String) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(id))
    }

    private fun getProducts() = viewModelScope.launch {
        productUseCases.getProducts.getAll().collectLatest { response ->
            when (response) {
                Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is Resource.Success -> {


                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            products = response.data.moreFilters(currentFilters)
                        )
                    }

                    _searchUiState.update {
                        it.copy(products = response.data)
                    }
                }
            }
        }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(filtersCount = 0)
        }
    }


    fun refreshProducts() {
        _uiState.update { it.copy(isRefreshing = true) }
        getProducts()
    }

    fun applyFilters(
        sharedWarehouseFilterUiState: SharedWarehouseFilterUiState
    ) {
        val filterCount = sharedWarehouseFilterUiState.run {
            categoryFilters.size + tags.size + stockFilters.size
        }

        currentFilters = sharedWarehouseFilterUiState

        _uiState.update { uiState ->
            uiState.copy(
                filtersCount = filterCount,
            )
        }

        getProducts()
    }
}