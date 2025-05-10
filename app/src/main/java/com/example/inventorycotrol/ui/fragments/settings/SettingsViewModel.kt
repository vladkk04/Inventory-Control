package com.example.inventorycotrol.ui.fragments.settings

import androidx.lifecycle.ViewModel
import com.example.inventorycotrol.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

}