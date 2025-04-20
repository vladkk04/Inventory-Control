package com.example.bachelorwork.ui.fragments.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.local.entities.UserEntity
import com.example.bachelorwork.domain.model.auth.SignInRequest
import com.example.bachelorwork.domain.model.auth.SignUpRequest
import com.example.bachelorwork.domain.model.validator.InputValidator
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.domain.usecase.user.UserUseCases
import com.example.bachelorwork.ui.model.auth.signUp.SignUpUiEvent
import com.example.bachelorwork.ui.model.auth.signUp.SignUpUiFormState
import com.example.bachelorwork.ui.model.auth.signUp.SignUpUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(SignUpUiFormState())
    val uiFormState = _uiFormState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {

            is SignUpUiEvent.FullNameChanged -> {
                _uiFormState.update { it.copy(fullName = event.fullName, fullNameError = null) }
            }

            is SignUpUiEvent.EmailChanged -> {
                _uiFormState.update { it.copy(email = event.email, emailError = null) }
            }

            is SignUpUiEvent.PasswordChanged -> {
                _uiFormState.update { it.copy(password = event.password, passwordError = null) }
            }

            is SignUpUiEvent.PasswordConfirmChanged -> {
                _uiFormState.update {
                    it.copy(
                        confirmPassword = event.password,
                        confirmPasswordError = null
                    )
                }
            }

            SignUpUiEvent.NavigateBack -> {
                navigateBack()
            }

            SignUpUiEvent.NavigateToSignIn -> {
                navigateSignIn()
            }

            SignUpUiEvent.SignUp -> {
                if (!isValidateInputsForm()) return
                signUp()
            }

        }
    }

    private fun signUp() = viewModelScope.launch {
        authRepository.signUp(
            SignUpRequest(
                _uiFormState.value.fullName,
                uiFormState.value.email,
                uiFormState.value.password
            )
        ).collectLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is ApiResponseResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    signIn()
                }
            }

        }
    }

    private suspend fun signIn() {
        authRepository.signIn(
            SignInRequest(
                uiFormState.value.email,
                uiFormState.value.password
            )
        ).collectLatest { response ->
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
                    userUseCases.createUserUseCase.invoke(
                        UserEntity(
                            id = authRepository.getUserId().firstOrNull() ?: "",
                            fullName = uiFormState.value.fullName,
                            email = uiFormState.value.email,
                            imageUrl = null,
                        )
                    )

                    navigateToOrganisationsInvitingListAfterSignUp()
                }
            }
        }
    }

    private fun isValidateInputsForm(): Boolean {

        val fullNameInputValidator = InputValidator.create()
            .withNotEmpty()
            .build()
            .invoke(_uiFormState.value.fullName)

        val emailInputValidator = InputValidator.create()
            .withNotEmpty()
            .withEmail()
            .build()
            .invoke(_uiFormState.value.email)

        val passwordInputValidator = InputValidator.create()
            .withNotEmpty()
            .withPasswordConfirmation(
                _uiFormState.value.password,
                _uiFormState.value.confirmPassword
            )
            .build()
            .invoke(_uiFormState.value.password)

        _uiFormState.update {
            it.copy(
                fullNameError = fullNameInputValidator.errorMessage,
                emailError = emailInputValidator.errorMessage,
                passwordError = passwordInputValidator.errorMessage,
                confirmPasswordError = passwordInputValidator.errorMessage
            )
        }

        return !(emailInputValidator.hasError || passwordInputValidator.hasError || fullNameInputValidator.hasError)
    }

    private fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun navigateSignIn() = viewModelScope.launch {
        navigator.navigate(Destination.SignIn) {
            popUpTo(Destination.SignUp) {
                inclusive = true
                saveState = false
            }
            restoreState = false
        }
    }

    private fun navigateToOrganisationsInvitingListAfterSignUp() = viewModelScope.launch {
        navigator.navigate(Destination.InvitationListAfterSignUp) {
            popUpTo(Destination.Authentication) {
                inclusive = true
                saveState = false
            }
            restoreState = false
        }
    }
}