package com.example.inventorycotrol.ui.fragments.warehouse.productDetail.overview

import androidx.lifecycle.SavedStateHandle
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.BaseDetailViewModel
import com.example.inventorycotrol.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProductDetailOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productUseCases: ProductUseCases,
    private val navigator: AppNavigator,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseDetailViewModel(savedStateHandle, productUseCases, dispatcher) {


}