package com.example.bachelorwork.ui.fragments.warehouse.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.model.filters.WarehouseFilterUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
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
class WarehouseFilterViewModel @Inject constructor(
    private val categoryUseCases: ProductCategoryUseCases,
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WarehouseFilterUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllCategories()
    }

    private fun getAllCategories() = viewModelScope.launch {
        categoryUseCases.getCategories.getAll().onEach { result ->
            when (result) {
                Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    _uiState.update { it.copy(categories = result.data) }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun navigateBack (selectedFiltersCount: Int) = viewModelScope.launch {
        navigator.navigateUp(mapOf("selectedFiltersCount" to selectedFiltersCount))
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }


}