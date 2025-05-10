package com.example.inventorycotrol.ui.fragments.warehouse.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.product.ProductViewDisplayMode
import com.example.inventorycotrol.domain.model.product.SortBy
import com.example.inventorycotrol.domain.model.sorting.SortDirection
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
import com.example.inventorycotrol.ui.fragments.warehouse.filters.SharedWarehouseFilterUiState
import com.example.inventorycotrol.ui.model.product.list.ProductListUIState
import com.example.inventorycotrol.ui.model.product.list.ProductSearchUIState
import com.example.inventorycotrol.ui.model.product.list.moreFilters
import com.example.inventorycotrol.ui.model.product.list.sortBy
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
    private val userUseCases: UserUseCases,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _searchUiState = MutableStateFlow(ProductSearchUIState())
    val searchUiState get() = _searchUiState.asStateFlow()

    private val _thresholdSettings = MutableStateFlow(
        ThresholdSettings(100.00, 50.00, 25.00)
    )
    val thresholdSettings = _thresholdSettings.asStateFlow()

    private var currentFilters: SharedWarehouseFilterUiState = SharedWarehouseFilterUiState()

    init {
        getThresholdSettings()
        getProducts()
        viewModelScope.launch {
            userUseCases.getUserUseCase.get().onEach { response ->
                when (response) {
                    Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Error -> {
                        sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(imageUrl = response.data.imageUrl)
                        }
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getThresholdSettings() = viewModelScope.launch {
        val normal =
            dataStoreManager.getPreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE).firstOrNull()
                ?: 100.00
        val medium =
            dataStoreManager.getPreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE).firstOrNull()
                ?: 50.00
        val critical =
            dataStoreManager.getPreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE).firstOrNull()
                ?: 25.00

        _thresholdSettings.update {
            it.copy(
                normalThresholdPercentage = normal,
                mediumThresholdPercentage = medium,
                criticalThresholdPercentage = critical
            )
        }
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

    fun navigateToItemDetail(id: String) = viewModelScope.launch {
        navigator.navigate(Destination.ProductDetail(id))
    }

    fun openNavigationDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }


    private fun getProducts() = viewModelScope.launch {
        productUseCases.getProducts.getAll().onEach { response ->
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
        }.launchIn(viewModelScope)
    }

    fun clearFilters() {
        _uiState.update {
            currentFilters = SharedWarehouseFilterUiState()
            it.copy(filtersCount = 0)
        }
        getProducts()
    }

    fun refreshProducts() {
        _uiState.update { it.copy(isRefreshing = true) }
        getProducts()
    }

    fun applyFilters(
        sharedWarehouseFilterUiState: SharedWarehouseFilterUiState
    ) {
        val filterCount = sharedWarehouseFilterUiState.let {
            val categoryFilters = it.categoryFilters
            val tags = it.tags
            val stockFilters = it.stockFilters
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