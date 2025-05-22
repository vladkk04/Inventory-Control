package com.example.inventorycotrol.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val authRepository: AuthRemoteDataSource,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileUseCases.getProfile.getProfile().distinctUntilChanged().onEach { result ->
                when (result) {
                    Resource.Loading -> {


                    }

                    is Resource.Error -> {
                        result.data?.let {
                            _uiState.update {
                                it.copy(
                                    id = result.data.id,
                                    fullName = result.data.fullName,
                                    logoUrl = result.data.imageUrl,
                                    email = result.data.email
                                )
                            }
                        }
                        sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    }

                    is Resource.Success -> {
                        if (result.data != null) {
                            _uiState.update {
                                it.copy(
                                    id = result.data.id,
                                    fullName = result.data.fullName,
                                    logoUrl = result.data.imageUrl,
                                    email = result.data.email
                                )
                            }
                        }
                    }

                }
            }.launchIn(viewModelScope)
        }
    }

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut().distinctUntilChanged().collect {
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

    fun navigateToProfileEdit() = viewModelScope.launch {
        navigator.navigate(Destination.EditProfile(_uiState.value.logoUrl, _uiState.value.fullName))
    }


}