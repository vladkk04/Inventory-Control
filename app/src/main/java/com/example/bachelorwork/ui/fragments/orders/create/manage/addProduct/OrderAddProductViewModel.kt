package com.example.bachelorwork.ui.fragments.orders.create.manage.addProduct

import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.orders.create.manage.BaseOrderManageAddedProductViewModel
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderAddProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
) : BaseOrderManageAddedProductViewModel(productUseCase, navigator, barcodeScannerUseCase)
