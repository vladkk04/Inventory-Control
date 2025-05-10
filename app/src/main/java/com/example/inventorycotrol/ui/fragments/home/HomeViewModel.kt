package com.example.inventorycotrol.ui.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.domain.usecase.organisatonSettings.OrganisationSettingsUseCases
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
import com.example.inventorycotrol.ui.model.home.FilterCriticalItems
import com.example.inventorycotrol.ui.model.home.HomeUiState
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockCompact
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
import kotlin.math.ceil

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val productUseCases: ProductUseCases,
    private val userUseCases: UserUseCases,
    private val productCategoriesUseCase: ProductCategoryUseCases,
    private val stockUpdateStockUseCases: ProductUpdateStockUseCases,
    private val organisationUserUseCases: OrganisationUserUseCases,
    private val organisationUseCases: OrganisationUseCases,
    private val organisationSettingsUseCases: OrganisationSettingsUseCases,
    private val dataStoreManager: DataStoreManager
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
        getOrganisation()
        getCategories()
        getOrganisationUsers()
        getOrganisationSettings()
        getCriticalStockItems(_uiState.value.filterCriticalItems)
        getLastStockTransaction()
    }

    private fun getOrganisation() = viewModelScope.launch {
        organisationUseCases.get.getOrganisation().onEach { result ->
            when (result) {
                Resource.Loading -> {

                }

                is Resource.Error -> {

                }

                is Resource.Success -> {
                    dataStoreManager.savePreference(AppConstants.ORGANISATION_CURRENCY to result.data.currency)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getOrganisationSettings() {
        organisationSettingsUseCases.get().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Resource.Success -> {
                    val data = result.data
                    _uiState.update {
                        it.copy(
                            notificationSettings = data.notificationSettings,
                            thresholdSettings = data.thresholdSettings,
                        )
                    }

                    dataStoreManager.savePreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE to data.thresholdSettings.normalThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE to data.thresholdSettings.mediumThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE to data.thresholdSettings.criticalThresholdPercentage)
                }
            }
        }.launchIn(viewModelScope)
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
                    Log.d("debug", "FOUND ORG USERS")

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
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun getLastStockTransaction() {
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
                    Log.d("debug", "FOUND ORG LAST STOCK")

                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    val compactItems = response.data.map {
                        ProductUpdateStockCompact(
                            id = it.id,
                            totalUpdate = it.products.size,
                            updateAt = it.updatedAt,
                            "Uknown"
                        )
                    }.sortedByDescending { it.updateAt }

                    _uiState.update {
                        it.copy(
                            productUpdateStockItems = compactItems,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun getCriticalStockItems(filter: FilterCriticalItems) {
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
                    val criticalThreshold =
                        dataStoreManager.getPreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE)
                            .firstOrNull()
                            ?: 25.00


                    val criticalProducts =
                        result.data.filter { product ->
                            product.quantity <= ceil(product.minStockLevel * (criticalThreshold / 100))
                        }

                    val filterList = when (filter) {
                        FilterCriticalItems.SHOW_ALL -> criticalProducts
                        FilterCriticalItems.LAST_100 -> criticalProducts.takeLast(100)
                        FilterCriticalItems.LAST_10 -> criticalProducts.takeLast(10)
                        FilterCriticalItems.LAST_50 -> criticalProducts.takeLast(50)
                    }

                    _uiState.update {
                        it.copy(
                            totalProductsCount = result.data.size,
                            criticalStockItems = filterList,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
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
        }.launchIn(viewModelScope)
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

    fun navigateToUpdateStock() = viewModelScope.launch {
        navigator.navigate(Destination.ProductUpdateStock)
    }

    fun openDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    fun setupCriticalStockFilter(filter: FilterCriticalItems) = viewModelScope.launch {
        _uiState.update { it.copy(filterCriticalItems = filter) }
        getCriticalStockItems(filter)
    }
}

