package com.example.bachelorwork.ui.views.inputs

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.toEntity
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.ui.model.category.CategoryUiState
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import com.example.bachelorwork.ui.utils.extensions.handleResult
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

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState get() = _uiState.asStateFlow()

    init { getCategories() }

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
            Log.d("debug", e.message.toString())
            when (e) {
                is SQLiteConstraintException -> {
                    sendSnackbarEvent(SnackbarEvent("Category already exists"))
                }
            }
        })
    }

    fun updateCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.updateCategory(category.toEntity())
        handleResult(result, onSuccess = {
            _uiState.update { it.copy(currentCategory = category) }
            sendSnackbarEvent(SnackbarEvent("Category updated"))
        }, onFailure = { e ->
            when (e) {
                is SQLiteConstraintException -> {
                    sendSnackbarEvent(SnackbarEvent("Category with this name already exists"))
                }
            }
        })
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        val result = categoryUseCase.deleteCategory(category.toEntity())
        handleResult(result, onSuccess = {
            sendSnackbarEvent(SnackbarEvent("Category deleted"))
        }, onFailure = { e ->

        })
    }

    fun selectCategory(category: ProductCategory) {
        _uiState.update { it.copy(currentCategory = category) }

    }
}