package com.example.bachelorwork.ui.fragments.auth.resetPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.validator.InputValidator
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.ui.model.auth.resetPassword.ResetPasswordUiEvent
import com.example.bachelorwork.ui.model.auth.resetPassword.ResetPasswordUiFormState
import com.example.bachelorwork.ui.model.auth.resetPassword.ResetPasswordUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val data = Destination.from<Destination.VerificationOtp>(savedStateHandle)

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ResetPasswordUiFormState())
    val uiFormState = _uiFormState.asStateFlow()

    fun onEvent(event: ResetPasswordUiEvent) {
        when (event) {
            is ResetPasswordUiEvent.PasswordChanged -> {
                _uiFormState.update {
                    it.copy(
                        password = event.password,
                        passwordError = null
                    )
                }
            }

            is ResetPasswordUiEvent.PasswordConfirmChanged -> {
                _uiFormState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordError = null
                    )
                }
            }

            ResetPasswordUiEvent.ResetPassword -> {
                if (!isValidateInputs()) return
                resetPassword()
            }
        }
    }

    private fun isValidateInputs(): Boolean {
        val validator = InputValidator
            .create()
            .withNotEmpty()
            .withPasswordConfirmation(_uiFormState.value.password, _uiFormState.value.confirmPassword)
            .build()
            .invoke(_uiFormState.value.password)

        _uiFormState.update {
            it.copy(
                passwordError = validator.errorMessage,
                confirmPasswordError = validator.errorMessage
            )
        }

        return !validator.hasError
    }

    private fun resetPassword() = viewModelScope.launch {
        authRepository.resetPassword(email = data.email, uiFormState.value.password).collectLatest {
            when (it) {
                ApiResponseResult.Loading -> {
                    _uiState.update { state -> state.copy(isLoading = true) }
                }
                is ApiResponseResult.Failure -> {
                    _uiState.update { state -> state.copy(isLoading = true) }
                    sendSnackbarEvent(SnackbarEvent(it.errorMessage))
                }
                is ApiResponseResult.Success -> {
                    _uiState.update { state -> state.copy(isLoading = true) }
                    navigateToSignIn()
                }
            }
        }
    }

    fun navigateUp() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun navigateToSignIn() = viewModelScope.launch {
        navigator.navigate(Destination.SignIn) {
            popUpTo(Destination.Authentication)
        }
    }

}