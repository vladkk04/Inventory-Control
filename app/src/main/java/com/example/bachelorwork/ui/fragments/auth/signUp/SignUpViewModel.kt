package com.example.bachelorwork.ui.fragments.auth.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.repository.AuthRepository
import com.example.bachelorwork.ui.model.auth.SignUpUiEvent
import com.example.bachelorwork.ui.model.auth.SignUpUiFormState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.utils.extensions.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    private val _uiFormState = MutableStateFlow(SignUpUiFormState())
    val uiFormState = _uiFormState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.CompanyNameChanged -> {
                _uiFormState.value =
                    _uiFormState.value.copy(companyName = event.name, companyNameError = null)
            }

            is SignUpUiEvent.EmailChanged -> {
                _uiFormState.value = _uiFormState.value.copy(email = event.email, emailError = null)
            }

            is SignUpUiEvent.FullNameChanged -> {
                _uiFormState.value =
                    _uiFormState.value.copy(email = event.fullName, emailError = null)
            }

            is SignUpUiEvent.PasswordChanged -> {
                _uiFormState.value =
                    _uiFormState.value.copy(password = event.password, passwordError = null)
            }

            is SignUpUiEvent.PasswordConfirmChanged -> {
                _uiFormState.value = _uiFormState.value.copy(
                    confirmPassword = event.password,
                    confirmPasswordError = null
                )
            }

            SignUpUiEvent.NavigateBack -> {
                navigateBack()
            }

            SignUpUiEvent.AgreeProcessingPersonalDataChanged -> {
                _uiFormState.value =
                    _uiFormState.value.copy(agreeProcessingPersonalData = !_uiFormState.value.agreeProcessingPersonalData)
            }

            SignUpUiEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp() = viewModelScope.launch {
        handleResult(
            authRepository.signUp(uiFormState.value.email, uiFormState.value.password),
            onSuccess = {

            },
            onFailure = { e ->
            Log.d("debug", e.message.toString())
            }
        )
    }

    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }
}