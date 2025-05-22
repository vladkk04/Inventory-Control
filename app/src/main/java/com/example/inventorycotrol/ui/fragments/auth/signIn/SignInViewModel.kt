package com.example.inventorycotrol.ui.fragments.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.auth.requests.SignInRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.model.auth.signIn.SignInUiEvent
import com.example.inventorycotrol.ui.model.auth.signIn.SignInUiFormState
import com.example.inventorycotrol.ui.model.auth.signIn.SignInUiState
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
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator,
    private val profileUseCases: ProfileUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(SignInUiFormState())
    val uiStateForm = _uiStateForm.asStateFlow()


    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.EmailChanged -> {
                _uiStateForm.value = _uiStateForm.value.copy(email = event.email.trim(), emailError = null)
            }

            is SignInUiEvent.PasswordChanged -> {
                _uiStateForm.value =
                    _uiStateForm.value.copy(password = event.password.trim(), passwordError = null)
            }

            SignInUiEvent.ForgotPassword -> {
                navigateToForgotPassword()
            }

            SignInUiEvent.NavigateBack -> {
                navigateBack()
            }

            SignInUiEvent.RememberMeChanged -> {

            }

            SignInUiEvent.SignIn -> {
                if (!isValidateForm()) return
                signIn()
            }

            SignInUiEvent.NavigateToSignUp -> {
                navigateToSignUp()
            }
        }
    }

    private fun signIn() = viewModelScope.launch {
        authRepository.signIn(
            SignInRequest(
                _uiStateForm.value.email,
                _uiStateForm.value.password
            )
        ).collectLatest { result ->
            when (result) {
                ApiResponseResult.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is ApiResponseResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigateToOrganisationInvitingListAfterSignUp()
                }
            }
        }
    }

    private fun navigateToOrganisationInvitingListAfterSignUp() = viewModelScope.launch {
        navigator.navigate(Destination.InvitationListAfterSignUp) {
            popUpTo(Destination.Authentication) {
                inclusive = true
                saveState = false
            }
            restoreState = false
        }
    }

    private fun navigateToHome() = viewModelScope.launch {
        navigator.navigate(Destination.Home) {
            popUpTo(Destination.Authentication) {
                inclusive = true
                saveState = false
            }
            restoreState = false
        }
    }

    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }


    private fun navigateToForgotPassword() = viewModelScope.launch {
        navigator.navigate(Destination.ForgotPassword)
    }

    private fun navigateToSignUp() = viewModelScope.launch {
        navigator.navigate(Destination.SignUp) {
            popUpTo(Destination.Authentication)
        }
    }


    private fun isValidateForm(): Boolean {

        val emailValidator = InputValidator
            .create()
            .withNotEmpty()
            .withEmail()
            .build()
            .invoke(_uiStateForm.value.email)

        val passwordValidator = InputValidator
            .create()
            .withNotEmpty()
            .build()
            .invoke(_uiStateForm.value.password)

        _uiStateForm.update {
            it.copy(
                emailError = emailValidator.errorMessage,
                passwordError = passwordValidator.errorMessage
            )
        }

        return !(emailValidator.hasError || passwordValidator.hasError)
    }

}