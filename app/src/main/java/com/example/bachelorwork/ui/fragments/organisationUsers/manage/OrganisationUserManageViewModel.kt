package com.example.bachelorwork.ui.fragments.organisationUsers.manage

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.user.UserUseCases
import com.example.bachelorwork.ui.model.organisationUser.OrganisationUserManageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrganisationUserManageViewModel @Inject constructor(
    private val userUseCase: UserUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationUserManageUiState())
    val uiState = _uiState.asStateFlow()

    //private val _uiStateForm = MutableStateFlow(BaseOrganisationUserInvitationFormEvent)


    init {

    }


    /*private val _uiStateForm = MutableStateFlow(AddUserToOrganisationFormState())
    val uiStateForm = _uiStateForm.asStateFlow()



    fun onEvent(event: AddUserToOrganisationFormEvent) {
        when (event) {
            is AddUserToOrganisationFormEvent.EmailChanged -> {
                emailQueryFlow.value = event.email
                _uiStateForm.update {
                    it.copy(
                        email = event.email,
                        emailError = null
                    )
                }
            }

            is AddUserToOrganisationFormEvent.OrganisationAddUserNameChanged -> _uiStateForm.update {
                it.copy(
                    organisationUserName = event.fullName,
                    organisationUserNameError = null
                )
            }

            AddUserToOrganisationFormEvent.InviteUser -> {
                if (validateInputFields()) {
                   // createUser()
                }
            }
        }
    }

    private fun findUserByEmail(email: String) = viewModelScope.launch {
        safeResponseApiCallFlow { userUseCase.getUserUseCase.getByEmail(email) }.collect { result ->
            when (result) {
                ApiResponseResult.Loading -> {

                }
                is ApiResponseResult.Failure -> {
                    _uiStateForm.update {
                        it.copy(
                            emailError = result.errorMessage
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isUserExist = false
                        )
                    }
                }
                is ApiResponseResult.Success -> {
                    _uiState.update {
                        it.copy(
                            organisationUserName = result.data.fullName
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isUserExist = true
                        )
                    }
                }
            }
        }
    }

    private fun validateInputFields(): Boolean {
        val emailInputValidator = InputValidator.create()
            .withNotEmpty()
            .withEmail()
            .build()
            .getAll(_uiStateForm.value.email)


        *//*val emailResult = validatorNotEmptyUseCase(_uiStateForm.value.email)
        val fullNameResult = validatorNotEmptyUseCase(_uiStateForm.value.fullName)
        val passwordResult = validatorNotEmptyUseCase(_uiStateForm.value.confirmPassword)

        val hasError = listOf(
            emailResult,
            fullNameResult,
            passwordResult
        ).any { !it.hasError }

        _uiStateForm.update {
            it.copy(
                emailError = emailResult.errorMessage,
                fullNameError = fullNameResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        return hasError*//*
        return false
    }*/
}