package com.example.bachelorwork.ui.fragments.orders.manage.manageProduct

import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderAddProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: AppNavigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
) : BaseOrderProductManageViewModel(productUseCase, navigator, barcodeScannerUseCase)
