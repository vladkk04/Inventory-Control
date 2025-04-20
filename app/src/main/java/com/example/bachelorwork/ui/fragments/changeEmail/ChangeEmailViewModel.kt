package com.example.bachelorwork.ui.fragments.changeEmail

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangeEmailUiState())
    val uiState = _uiState.asStateFlow()

}