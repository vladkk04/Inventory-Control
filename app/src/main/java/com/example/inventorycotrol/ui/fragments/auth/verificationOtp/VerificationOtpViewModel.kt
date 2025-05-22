package com.example.inventorycotrol.ui.fragments.auth.verificationOtp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.auth.requests.ForgotPasswordRequest
import com.example.inventorycotrol.domain.model.auth.requests.OtpRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.ui.model.auth.verificationOtp.VerificationOtpUiEvent
import com.example.inventorycotrol.ui.model.auth.verificationOtp.VerificationOtpUiFormState
import com.example.inventorycotrol.ui.model.auth.verificationOtp.VerificationOtpUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationOtpViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val data = Destination.from<Destination.VerificationOtp>(savedStateHandle)

    private val _uiState = MutableStateFlow(VerificationOtpUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(VerificationOtpUiFormState())
    val uiStateForm get() = _uiStateForm.asStateFlow()

    fun onEvent(event: VerificationOtpUiEvent) {
        when (event) {
            is VerificationOtpUiEvent.OtpChanged -> {
                _uiStateForm.update { it.copy(otp = event.otp.trim(), otpError = null) }
            }

            VerificationOtpUiEvent.Continue -> {
                if (!isValidatedInputs()) return
                isValidOtp()
            }

            VerificationOtpUiEvent.NavigateBack -> {
                navigateBack()
            }

            VerificationOtpUiEvent.ResendOtp -> {
                resendOtp()
            }
        }
    }

    private fun resendOtp() = viewModelScope.launch {
        authRepository.forgotPassword(ForgotPasswordRequest(data.email)).collectLatest { response ->
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
                    sendSnackbarEvent(SnackbarEvent("Send otp successfully"))
                }
            }
        }
    }

    private fun isValidOtp() = viewModelScope.launch {
        authRepository.validateOtp(OtpRequest(data.email, _uiStateForm.value.otp))
            .collectLatest { result ->
                when (result) {
                    ApiResponseResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is ApiResponseResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    }

                    is ApiResponseResult.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        navigateToResetPassword()
                    }
                }
            }
    }

    private fun isValidatedInputs(): Boolean {
        val otpValidation = InputValidator.create()
            .withNotEmpty()
            .withOtp()
            .build()
            .invoke(_uiStateForm.value.otp)

        _uiStateForm.update {
            it.copy(
                otpError = otpValidation.errorMessage
            )
        }

        return !otpValidation.hasError
    }

    private fun navigateToResetPassword() = viewModelScope.launch {
        navigator.navigate(Destination.ResetPassword(data.email)) {
            popUpTo(Destination.ForgotPassword) {
                saveState = true
            }
            restoreState = true
        }
    }

    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

}