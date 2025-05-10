package com.example.inventorycotrol.ui.fragments.organisationUsers.manage.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailFormEvent
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailFormState
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationUserInvitationEmailViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUserUseCase: OrganisationUserUseCases,
    private val userUseCase: UserUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationUserInvitationEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(OrganisationUserInvitationEmailFormState())
    val uiStateForm = _uiStateForm.asStateFlow()

    private val _canInvite = MutableStateFlow(false)
    val canInvite = _canInvite.asStateFlow()

    private val _organisationUserName = MutableStateFlow("")
    val organisationUserName = _organisationUserName.asStateFlow()

    @OptIn(FlowPreview::class)
    private val emailQueryFlow = MutableStateFlow("").apply {
        debounce(1000)
            .filterNot { emailValidator(it).hasError }
            .onEach { findUserByEmail(it) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: OrganisationUserInvitationEmailFormEvent) {
        when (event) {
            is OrganisationUserInvitationEmailFormEvent.OrganisationUserNameChanged -> {
                _uiStateForm.update { state ->
                    state.copy(
                        organisationUserName = event.organisationUserName,
                        organisationUserNameError = null
                    )
                }
                _canInvite.value = isValidateInputs()
            }

            is OrganisationUserInvitationEmailFormEvent.EmailChanged -> {
                emailQueryFlow.value = event.email
                _canInvite.value = false
                _uiStateForm.update { state ->
                    state.copy(email = event.email, emailError = null)
                }
            }

            is OrganisationUserInvitationEmailFormEvent.RoleChanged -> {
                _uiStateForm.update { state ->
                    state.copy(roleName = event.role.name, roleIdError = null)
                }
                _canInvite.value = isValidateInputs()
            }

            OrganisationUserInvitationEmailFormEvent.Invite -> {
                inviteUserByEmail()
            }
        }

    }

    private fun inviteUserByEmail() = viewModelScope.launch {
        organisationUserUseCase.invitationUserUseCase.inviteUserByEmail(
            OrganisationInvitationEmailRequest(
                organisationUserName = uiStateForm.value.organisationUserName,
                email = uiStateForm.value.email,
                organisationRole = OrganisationRole.valueOf(uiStateForm.value.roleName)
            )
        ).onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent("Invitation sent"))
                    navigator.navigateUp()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun findUserByEmail(email: String) = viewModelScope.launch {
        userUseCase.getUserUseCase.getByEmail(email).collectLatest { result ->
            when (result) {
                ApiResponseResult.Loading -> {}

                is ApiResponseResult.Failure -> {
                    if (result.code == 404) {
                        _uiStateForm.update {
                            it.copy(
                                emailError = null
                            )
                        }
                        _uiState.update {
                            it.copy(
                                inviteAsNewUser = true
                            )
                        }
                        _canInvite.value = isValidateInputs()
                        return@collectLatest
                    }

                    _uiStateForm.update {
                        it.copy(
                            emailError = result.errorMessage
                        )
                    }
                }

                is ApiResponseResult.Success -> {
                    _uiState.update {
                        it.copy(
                            inviteAsNewUser = false
                        )
                    }
                    _canInvite.value = isValidateInputs()
                    _organisationUserName.value = result.data.fullName
                }
            }
        }
    }

    private fun emailValidator(value: String) = InputValidator
        .create()
        .withNotEmpty()
        .withEmail()
        .build()
        .invoke(value)


    private fun isValidateInputs(): Boolean {
        val notEmptyValidator = InputValidator
            .create()
            .withNotEmpty()
            .build()

        val organisationUserNameError =
            notEmptyValidator(uiStateForm.value.organisationUserName)
        val roleIdError = notEmptyValidator(uiStateForm.value.roleName)
        val emailError = emailValidator(uiStateForm.value.email)

        return !(organisationUserNameError.hasError || roleIdError.hasError || emailError.hasError)
    }
}