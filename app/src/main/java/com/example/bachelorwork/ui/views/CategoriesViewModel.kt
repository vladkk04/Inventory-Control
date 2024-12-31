package com.example.bachelorwork.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.toEntity
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.utils.extensions.handleResult
import com.example.bachelorwork.ui.utils.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCase: ProductCategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        val result = categoryUseCase.getCategories()

        handleResult(result, onSuccess = { categories ->
            _uiState.update { it.copy(categories = categories ) }
        }, onFailure = { e ->
            _uiState.update { it.copy(errorMessage = e.message ) }
        })
    }

    fun createCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.createCategory(category.toEntity())
        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Category created"))
        }, onFailure = { e ->
            _uiState.update { it.copy(errorMessage = e.message ) }
        })
    }

    fun updateCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.updateCategory(category.toEntity())
        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Category updated"))
        }, onFailure = { e ->
            _uiState.update { it.copy(errorMessage = e.message ) }
        })
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.deleteCategory(category.toEntity())
        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Category deleted"))
        }, onFailure = { e ->
            _uiState.update { it.copy(errorMessage = e.message ) }
        })
    }
}