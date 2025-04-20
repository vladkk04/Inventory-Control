package com.example.bachelorwork.ui.fragments.auth.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.auth.ForgotPasswordRequest
import com.example.bachelorwork.domain.model.validator.InputValidator
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiEvent
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiFormState
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiState
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
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(ForgotPasswordUiFormState())
    val uiStateForm get() = _uiStateForm.asStateFlow()


    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.EmailChanged -> _uiStateForm.update {
                it.copy(
                    email = event.email,
                    emailError = null
                )
            }

            ForgotPasswordUiEvent.NavigateBack -> navigateBack()
            ForgotPasswordUiEvent.Send -> sendOtpToEmail()
        }
    }


    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun navigateToVerificationOtp() = viewModelScope.launch {
        navigator.navigate(Destination.VerificationOtp(_uiStateForm.value.email)) {
            popUpTo(Destination.ForgotPassword) {
                saveState = true
                inclusive = false
            }
            restoreState = true
        }
    }

    private fun sendOtpToEmail() = viewModelScope.launch {
        if (!isValidatedInputs()) return@launch

        authRepository.forgotPassword(ForgotPasswordRequest(_uiStateForm.value.email))
            .collectLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is ApiResponseResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    }

                    is ApiResponseResult.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        navigateToVerificationOtp()
                    }
                }
            }

    }

    private fun isValidatedInputs(): Boolean {
        val emailValidation = InputValidator.create()
            .withNotEmpty()
            .withEmail()
            .build()
            .invoke(_uiStateForm.value.email)

        _uiStateForm.update {
            it.copy(
                emailError = emailValidation.errorMessage
            )
        }

        return !emailValidation.hasError
    }
}