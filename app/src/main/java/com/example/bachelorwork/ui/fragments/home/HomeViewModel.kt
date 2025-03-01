package com.example.bachelorwork.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: AppNavigator
): ViewModel() {

    fun openNavigationDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    fun navigateToCreateProduct() = viewModelScope.launch {
        navigator.navigate(Destination.CreateProduct)
    }

    fun navigateToCreateOrder() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrder)
    }

    fun navigateToCreateUser() = viewModelScope.launch {
        navigator.navigate(Destination.CreateNewUser)
    }

    fun navigateToUpdateProductsStock() = viewModelScope.launch {
        navigator.navigate(Destination.UpdateProductsStock)
    }

}