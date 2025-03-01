package com.example.bachelorwork.ui.fragments.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.validator.ValidatorInputFieldFactory
import com.example.bachelorwork.domain.model.validator.validators.ValidatorEmail
import com.example.bachelorwork.domain.model.validator.validators.ValidatorNotEmpty
import com.example.bachelorwork.domain.model.validator.validators.ValidatorPassword
import com.example.bachelorwork.domain.repository.AuthRepository
import com.example.bachelorwork.ui.model.auth.SignInUiEvent
import com.example.bachelorwork.ui.model.auth.SignInUiFormState
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    private val _uiStateForm = MutableStateFlow(SignInUiFormState())
    val uiStateForm = _uiStateForm.asStateFlow()


    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.EmailChanged -> {
                _uiStateForm.value = _uiStateForm.value.copy(email = event.email, emailError = null)
            }
            is SignInUiEvent.PasswordChanged -> {
                _uiStateForm.value = _uiStateForm.value.copy(password = event.password, passwordError = null)
            }

            SignInUiEvent.ForgotPassword -> { navigateToForgotPassword() }

            SignInUiEvent.NavigateBack -> { navigateBack() }

            SignInUiEvent.RememberMeChanged -> {

            }
            SignInUiEvent.SignIn -> {
                if (!isValidateForm()) return

                signIn()
            }

        }
    }

    private fun signIn() = viewModelScope.launch {

    }

    private fun navigateToForgotPassword() = viewModelScope.launch {
        //navigator.navigate(Destination.ForgotPassword)
    }

    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun isValidateForm(): Boolean {

        val validatorEmailFactory = ValidatorInputFieldFactory(
            inputs = arrayOf(_uiStateForm.value.email),
            validators = setOf(ValidatorNotEmpty, ValidatorEmail)
        )

        val validatorPasswordFactory = ValidatorInputFieldFactory(
            inputs = arrayOf(_uiStateForm.value.password),
            validators = setOf(ValidatorNotEmpty, ValidatorPassword)
        )


        _uiStateForm.value = _uiStateForm.value.copy(
            emailError = validatorEmailFactory.errorMessages[_uiStateForm.value.email],
            passwordError = validatorPasswordFactory.errorMessages[_uiStateForm.value.password]
        )

        return validatorEmailFactory.hasError && validatorPasswordFactory.hasError
    }

}