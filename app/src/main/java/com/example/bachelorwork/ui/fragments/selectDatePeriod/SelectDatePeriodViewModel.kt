package com.example.bachelorwork.ui.fragments.selectDatePeriod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.TimePeriod
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SelectDatePeriodViewModel @Inject constructor(
    private val navigator: AppNavigator,
) : ViewModel() {


    fun selectPeriod(datePeriod: TimePeriod) = viewModelScope.launch {
        navigator.navigateUp(mapOf("datePeriod" to datePeriod))
    }


}