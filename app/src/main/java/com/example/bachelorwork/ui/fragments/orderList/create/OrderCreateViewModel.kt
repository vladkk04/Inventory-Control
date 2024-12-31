package com.example.bachelorwork.ui.fragments.orderList.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCreateViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun navigateToOrderProductPicker() = viewModelScope.launch {
        navigator.navigate(Destination.AddItemToOrder)
    }
}
