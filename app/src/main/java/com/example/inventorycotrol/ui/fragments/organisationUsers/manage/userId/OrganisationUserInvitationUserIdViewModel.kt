package com.example.inventorycotrol.ui.fragments.organisationUsers.manage.userId

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormEvent
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormState
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationUserInvitationUserIdViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val userUseCase: UserUseCases,
    private val organisationUserUseCase: OrganisationUserUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationUserInvitationUserIdUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(OrganisationUserInvitationUserIdFormState())
    val uiStateForm = _uiStateForm.asStateFlow()

    private val _canInvite = MutableStateFlow(false)
    val canInvite = _canInvite.asStateFlow()

    private val _organisationUserName = MutableStateFlow("")
    val organisationUserName = _organisationUserName.asStateFlow()


    @OptIn(FlowPreview::class)
    private val userIdQueryFlow = MutableStateFlow("").apply {
        debounce(2000)
            .filterNot { it.isBlank() }
            .onEach { findUserByUserId(it) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: OrganisationUserInvitationUserIdFormEvent) {
        when (event) {
            is OrganisationUserInvitationUserIdFormEvent.OrganisationUserNameChanged -> {
                _uiStateForm.update { state ->
                    state.copy(
                        organisationUserName = event.organisationUserName,
                        organisationUserNameError = null
                    )
                }
                _canInvite.value = isValidateInputs()
            }

            is OrganisationUserInvitationUserIdFormEvent.UserIdChanged -> {
                userIdQueryFlow.value = event.userId
                _canInvite.value = false
                _uiStateForm.update { state ->
                    state.copy(userId = event.userId, userIdError = null)
                }
            }

            is OrganisationUserInvitationUserIdFormEvent.RoleChanged -> {
                _uiStateForm.update { state ->
                    state.copy(roleId = event.roleId, roleIdError = null)
                }
                _canInvite.value = isValidateInputs()
            }

            OrganisationUserInvitationUserIdFormEvent.Invite -> {
                inviteUserByEmail()
            }
        }
    }

    private fun inviteUserByEmail() = viewModelScope.launch {
        organisationUserUseCase.invitationUserUseCase.inviteUserByUserId(
            OrganisationInvitationUserIdRequest(
                organisationUserName = uiStateForm.value.organisationUserName,
                userId = uiStateForm.value.userId,
                organisationRole = OrganisationRole.valueOf(uiStateForm.value.roleId)
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

    private fun findUserByUserId(userId: String) = viewModelScope.launch {
        userUseCase.getUserUseCase.getById(userId).collect { result ->
            when (result) {
                ApiResponseResult.Loading -> {}

                is ApiResponseResult.Failure -> {
                    _uiStateForm.update {
                        it.copy(userIdError = result.errorMessage)
                    }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    _canInvite.value = isValidateInputs()
                    _organisationUserName.value = result.data.fullName
                }
            }
        }
    }

    private fun isValidateInputs(): Boolean {
        val notEmptyValidator = InputValidator
            .create()
            .withNotEmpty()
            .build()

        val organisationUserNameError = notEmptyValidator(uiStateForm.value.organisationUserName)
        val roleIdError = notEmptyValidator(uiStateForm.value.roleId)
        val userIdError = notEmptyValidator(uiStateForm.value.userId)

        return !(organisationUserNameError.hasError || roleIdError.hasError || userIdError.hasError || _uiStateForm.value.userIdError != null)
    }



}