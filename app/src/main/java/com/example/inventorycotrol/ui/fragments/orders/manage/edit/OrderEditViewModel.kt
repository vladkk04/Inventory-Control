package com.example.inventorycotrol.ui.fragments.orders.manage.edit

import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.ui.fragments.orders.manage.BaseOrderManageViewModel
import com.example.inventorycotrol.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val navigator: AppNavigator,
    private val dataStoreManager: DataStoreManager
): BaseOrderManageViewModel(productUseCases, navigator, dataStoreManager) {



}