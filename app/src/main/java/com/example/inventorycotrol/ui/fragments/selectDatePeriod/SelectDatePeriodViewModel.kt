package com.example.inventorycotrol.ui.fragments.selectDatePeriod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.domain.model.TimePeriod
import com.example.inventorycotrol.ui.navigation.AppNavigator
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