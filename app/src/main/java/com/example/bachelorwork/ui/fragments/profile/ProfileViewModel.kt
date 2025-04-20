package com.example.bachelorwork.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val authRepository: AuthRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut().collect {
            when (it) {
                ApiResponseResult.Loading -> {}

                is ApiResponseResult.Failure -> {}
                is ApiResponseResult.Success -> {
                    navigator.navigate(Destination.Authentication) {
                        popUpTo(Destination.Home) {
                            inclusive = true
                            saveState = false
                        }
                        restoreState = false
                    }
                }
            }
        }
    }

    fun navigateToChangePassword() = viewModelScope.launch {
        navigator.navigate(Destination.ChangePassword)
    }

    fun navigateToChangeEmail() = viewModelScope.launch {
        navigator.navigate(Destination.ChangeEmail)
    }


}