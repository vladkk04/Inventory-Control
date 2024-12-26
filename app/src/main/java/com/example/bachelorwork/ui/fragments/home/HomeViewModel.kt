package com.example.bachelorwork.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {

    fun navigateToCreateItem() = viewModelScope.launch {
        navigator.navigate(Destination.ProductManage())
    }

    fun openNavigationDrawer() = viewModelScope.launch {
        navigator.openNavigationDrawer()
    }

    private fun navigateToCreateOrder() = viewModelScope.launch {
       // navigator.navigate(Destination.CreateNewUser)
    }

    fun navigateToCreateUser() = viewModelScope.launch {
        navigator.navigate(Destination.CreateNewUser)
    }

}