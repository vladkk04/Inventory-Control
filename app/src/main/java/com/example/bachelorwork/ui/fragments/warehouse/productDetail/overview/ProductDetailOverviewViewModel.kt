package com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview

import androidx.lifecycle.SavedStateHandle
import com.example.bachelorwork.di.IoDispatcher
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.BaseDetailViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
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