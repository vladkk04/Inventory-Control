package com.example.bachelorwork.ui.fragments.orders.create.manage.editAddedProduct

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.usecase.barcodeScanner.BarcodeScannerUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.ui.fragments.orders.create.manage.BaseOrderManageAddedProductViewModel
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderEditAddedProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCases,
    private val navigator: Navigator,
    private val barcodeScannerUseCase: BarcodeScannerUseCase,
    savedStateHandle: SavedStateHandle
) : BaseOrderManageAddedProductViewModel(productUseCase, navigator, barcodeScannerUseCase) {

    private val routeEditAddedProductArgs =
        Destination.from<Destination.OrderEditAddedProduct>(savedStateHandle)

    val id = routeEditAddedProductArgs.id
    val rate = routeEditAddedProductArgs.rate
    val quantity = routeEditAddedProductArgs.quantity

    init {
        viewModelScope.launch {
            handleResult(productUseCase.getProducts.getProductById(routeEditAddedProductArgs.id), onSuccess = {
                performSearch(it.name, ignoreCase = false)
            })
            delay(100)
            selectItem(routeEditAddedProductArgs.id)
        }
    }


}
