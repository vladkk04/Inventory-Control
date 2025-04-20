package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.navigation.Destination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val productUseCases: ProductUseCases,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    protected val productRoute = Destination.from<Destination.ProductDetail>(savedStateHandle)

    val product = MutableStateFlow<Product?>(null)
    val productState = product.asStateFlow()

    init {
        getProductDetail()
    }


    private fun getProductDetail() = viewModelScope.launch(dispatcher) {
        productUseCases.getProducts.getById(productRoute.id).collectLatest { response ->
            when (response) {
                Resource.Loading -> Unit
                is Resource.Error -> Unit
                is Resource.Success -> {
                    product.update { response.data }
                }
            }
        }
    }



}