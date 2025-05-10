package com.example.inventorycotrol.ui.fragments.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ChangePasswordUiFormState())
    val uiFormState = _uiFormState.asStateFlow()

    init {
        isValidateInputs()
    }

    fun onEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.ConfirmPassword -> {
                _uiFormState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordError = null
                    )
                }
                isValidateInputs()
            }

            is ChangePasswordUiEvent.NewPassword -> {
                _uiFormState.update {
                    it.copy(
                        newPassword = event.newPassword,
                        newPasswordError = null
                    )
                }
                isValidateInputs()
            }

            ChangePasswordUiEvent.ChangePassword -> {
                changePassword()
            }
        }
    }

    fun changePassword() = viewModelScope.launch {
        profileUseCases.changePassword(
            _uiFormState.value.newPassword,
            _uiFormState.value.confirmPassword
        ).onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ApiResponseResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    navigateBack()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun isValidateInputs(): Boolean {
        val validator = InputValidator
            .create()
            .withNotEmpty()
            .withPasswordConfirmation(
                _uiFormState.value.newPassword,
                _uiFormState.value.confirmPassword
            )
            .build()
            .invoke(_uiFormState.value.newPassword)

        _uiFormState.update {
            it.copy(
                newPasswordError = validator.errorMessage,
                confirmPasswordError = validator.errorMessage
            )
        }

        return !validator.hasError
    }

}