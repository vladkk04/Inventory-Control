package com.example.bachelorwork.ui.fragments.users

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.ui.model.user.CreateUserUiState
import com.example.bachelorwork.ui.model.user.UserManageFormEvent
import com.example.bachelorwork.ui.model.user.UserManageFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.security.SecureRandom
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUserUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(UserManageFormState())
    val uiStateForm = _uiStateForm.asStateFlow()

    fun createUser() {
        if (validateInputFields()) { return }
    }

    fun generatePassword() {
        _uiState.update { it.copy(randomPassword = generateSecureRandomPassword()) }
    }

    fun onEvent(event: UserManageFormEvent) {
        when (event) {
            is UserManageFormEvent.EmailChanged -> _uiStateForm.update {
                it.copy(
                    email = event.email,
                    emailError = null
                )
            }

            is UserManageFormEvent.FullNameChanged -> _uiStateForm.update {
                it.copy(
                    fullName = event.fullName,
                    fullNameError = null
                )
            }

            is UserManageFormEvent.PasswordChanged -> _uiStateForm.update {
                it.copy(
                    password = event.password,
                    passwordError = null
                )
            }
        }
    }

    private fun generateSecureRandomPassword(length: Int = 10): String {
        val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9') +
                listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')')
        val secureRandom = SecureRandom()
        return (1..length)
            .map { charset[secureRandom.nextInt(charset.size)] }
            .joinToString("")
    }

    private fun validateInputFields(): Boolean {
        /*val emailResult = validatorNotEmptyUseCase(_uiStateForm.value.email)
        val fullNameResult = validatorNotEmptyUseCase(_uiStateForm.value.fullName)
        val passwordResult = validatorNotEmptyUseCase(_uiStateForm.value.password)

        val hasError = listOf(
            emailResult,
            fullNameResult,
            passwordResult
        ).any { !it.success }

        _uiStateForm.update {
            it.copy(
                emailError = emailResult.errorMessage,
                fullNameError = fullNameResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        return hasError*/
        return false
    }
}