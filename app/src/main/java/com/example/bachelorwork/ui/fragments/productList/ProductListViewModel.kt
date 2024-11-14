package com.example.bachelorwork.ui.fragments.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.product.ProductOrder
import com.example.bachelorwork.domain.model.product.ProductViewType
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.product.SortDirection
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            /*repeat(100) {
                val product = Product(
                    categoryId = 1,
                    name = "${('a'..'z').random()} ${(0..199).random()}",
                    barcode = "skdfj",
                    quantity = (0..1000).random(),
                    totalPrice = (0..50).random().toDouble(),
                    minStockLevel = 20,
                    description = "fds",
                    datePurchase = Date(),
                    price = 20.00,
                    productUnit = ProductUnit.PCS,
                    tags = listOf(
                        ProductTag(name = "fds", R.drawable.ic_search_product),
                        ProductTag(name = "fds", R.drawable.ic_edit),
                        ProductTag(name = "fds", R.drawable.ic_view_grid)
                    )
                )

                productUseCases.createProduct.invoke(product)
            }*/
        }

        //productUseCases.getProducts.getgg(ProductOrder())
        getProducts(_uiState.value.orderBy)
    }

    fun changeViewType() {
        _uiState.update {
            it.copy(
                viewType = ProductViewType.entries[(it.viewType.ordinal + 1) % 2]
            )
        }
    }

    fun getProductsChangeSortBy(sortBy: SortBy) {
        when (sortBy) {
            SortBy.NAME -> {
                getProducts(
                    uiState.value.orderBy.copy(
                        sortBy = SortBy.NAME
                    )
                )
            }

            SortBy.PRICE -> {
                getProducts(
                    uiState.value.orderBy.copy(
                        sortBy = SortBy.PRICE
                    )
                )
            }
        }
    }

    fun getProductsChangeSortDirection() {
        getProducts(
            uiState.value.orderBy.copy(
                sortDirection = SortDirection.entries[(uiState.value.orderBy.sortDirection.ordinal + 1) % 2]
            )
        )
    }


    private fun getProducts(orderBy: ProductOrder) {
        productUseCases.getProducts(orderBy).onEach { result ->
            result.fold(
                onSuccess = { products ->
                    _uiState.value = _uiState.value.copy(
                        products = products,
                        orderBy = orderBy
                    )
                },
                onFailure = {
                    /*_uiState.value = _uiState.value.copy(
                        products = emptyList()
                    )*/
                }
            )
        }.launchIn(viewModelScope)
    }
}