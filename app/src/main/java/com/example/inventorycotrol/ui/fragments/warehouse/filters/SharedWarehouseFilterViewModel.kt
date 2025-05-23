package com.example.inventorycotrol.ui.fragments.warehouse.filters

import androidx.lifecycle.ViewModel
import com.example.inventorycotrol.domain.model.category.ProductCategory
import com.example.inventorycotrol.domain.model.product.ProductTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SharedWarehouseFilterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SharedWarehouseFilterUiState())
    val uiState = _uiState.asStateFlow()

    fun setupFilters(categoryFilters: List<ProductCategory>, stockFilters: List<StockFilter>, tags: List<ProductTag>) {
        _uiState.update {
            it.copy(
                categoryFilters = categoryFilters,
                stockFilters = stockFilters,
                tags = tags
            )
        }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                categoryFilters = emptyList(),
                stockFilters = emptyList(),
                tags = emptyList()
            )
        }
    }
}