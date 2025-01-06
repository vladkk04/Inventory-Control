package com.example.bachelorwork.ui.fragments.orders.detail

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {

}