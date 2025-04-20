package com.example.bachelorwork.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUser
import com.example.bachelorwork.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.bachelorwork.domain.usecase.user.UserUseCases
import com.example.bachelorwork.ui.model.home.HomeUiState
import com.example.bachelorwork.ui.model.productUpdateStock.ProductUpdateStockCompact
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
    private val userUseCases: UserUseCases,
    private val productCategoriesUseCase: ProductCategoryUseCases,
    private val stockUpdateStockUseCases: ProductUpdateStockUseCases,
    private val organisationUserUseCases: OrganisationUserUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        getCriticalStockItems()
        getCategories()
        getOrganisationUsers()
    }

    private suspend fun getOrganisationUsers() {
        organisationUserUseCases.getOrganisationUsersUseCase().onEach { response ->
            when (response) {
                Resource.Loading -> {
                    if (!_uiState.value.isRefreshing) {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            totalUsersCount = response.data.size,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    getLastStockTransaction(response.data)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private suspend fun getLastStockTransaction(data: List<OrganisationUser>) {
        stockUpdateStockUseCases.getStock.getAllByOrganisationView().onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    if (!_uiState.value.isRefreshing) {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
                is ApiResponseResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is ApiResponseResult.Success -> {
                    val compactItems = response.data.map {
                        ProductUpdateStockCompact(
                            id = it.id,
                            totalUpdate = it.products.size,
                            operationType = it.operationType,
                            updateAt = it.updatedAt,
                            updateBy = data.find { user -> user.userId == it.updatedBy }?.organisationUserName
                                ?: "Unknown user"
                        )
                    }

                    _uiState.update {
                        it.copy(
                            productUpdateStockItems = compactItems,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getCategories() {
        productCategoriesUseCase.getCategories.getAll().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    if (!_uiState.value.isRefreshing) {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private suspend fun getCriticalStockItems() {
        productUseCases.getProducts.getAll().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    if (!_uiState.value.isRefreshing) {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }
                is Resource.Success -> {
                    val criticalProducts = result.data.filter { it.quantity < it.minStockLevel * 0.25 }

                    _uiState.update {
                        it.copy(
                            totalProductsCount = result.data.size,
                            criticalStockItems = criticalProducts,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun navigateToCreateProduct() = viewModelScope.launch {
        navigator.navigate(Destination.CreateProduct)
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }

    fun navigateToInviteUser() = viewModelScope.launch {
        navigator.navigate(Destination.OrganisationManageUser)
    }

    fun navigateToUpdateProductsStock() = viewModelScope.launch {
        navigator.navigate(Destination.UpdateProductsStock)
    }

    fun navigateToStockIn() = viewModelScope.launch {
        navigator.navigate(Destination.StockIn)
    }

    fun navigateToStockOut() = viewModelScope.launch {
        navigator.navigate(Destination.StockOut)
    }
}

