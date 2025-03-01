package com.example.bachelorwork.ui.fragments.orders.manage.edit

import com.example.bachelorwork.ui.fragments.orders.manage.BaseOrderManageViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditViewModel @Inject constructor(
    private val navigator: AppNavigator
): BaseOrderManageViewModel(navigator) {

    fun updateOrder() {

    }


}