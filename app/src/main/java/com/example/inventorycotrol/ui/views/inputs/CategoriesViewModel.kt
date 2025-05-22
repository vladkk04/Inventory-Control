package com.example.inventorycotrol.ui.views.inputs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.category.ProductCategory
import com.example.inventorycotrol.domain.model.category.ProductCategoryRequest
import com.example.inventorycotrol.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.inventorycotrol.ui.model.category.CategoryUiState
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCase: ProductCategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState get() = _uiState.asStateFlow()

    init { getCategories() }

    private fun getCategories() = viewModelScope.launch {
        categoryUseCase.getCategories.getAll().onEach { response ->

            when (response) {
                Resource.Loading -> {

                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(categories = response.data) }
                }
            }

        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun createCategory(request: ProductCategoryRequest) {
        categoryUseCase.createCategory(
            request
        ).onEach { response ->
            when (response) {
                is Resource.Loading -> {

                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Category created"))
                }

            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun updateCategory(categoryId: String, request: ProductCategoryRequest) = viewModelScope.launch {
        categoryUseCase.updateCategory.invoke(
            categoryId,
            request
        ).collectLatest { response ->
            when (response) {
                Resource.Loading -> {

                }
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))

                }
                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Updated"))
                }
            }
        }
    }

    fun deleteCategory(categoryId: String) = viewModelScope.launch {
        categoryUseCase.deleteCategory(categoryId).collectLatest {
            when (it) {
                Resource.Loading -> {

                }
                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(it.errorMessage))
                }
                is Resource.Success -> {
                    sendSnackbarEvent(SnackbarEvent("Category deleted"))
                }
            }
        }
    }

    fun selectCategory(category: ProductCategory) {
        _uiState.update { it.copy(currentCategory = category) }
    }
}