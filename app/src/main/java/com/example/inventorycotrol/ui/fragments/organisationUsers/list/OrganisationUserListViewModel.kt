package com.example.inventorycotrol.ui.fragments.organisationUsers.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.ui.model.organisationUser.OrganisationUserListUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationUserListViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUserUseCases: OrganisationUserUseCases,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationUserListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        //getOrganisationUsers()
    }

    fun getOrganisationUsers() = viewModelScope.launch {
        organisationUserUseCases.getOrganisationUsersUseCase().onEach { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    val ownId = dataStoreManager.getPreference(AppConstants.USER_ID_KEY).first()
                    _uiState.update {
                        it.copy(
                            organisationUsers = response.data?.map { user -> user.copy(isCanEdit = false) }
                                ?.sortedByDescending { user -> user.userId == ownId }
                                ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false,
                            ownId = ownId
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }

                is Resource.Success -> {
                    val ownId = dataStoreManager.getPreference(AppConstants.USER_ID_KEY).first()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            organisationUsers = response.data.sortedByDescending { user -> user.userId == ownId },
                            ownId = ownId
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToManageUser() = viewModelScope.launch {
        navigator.navigate(Destination.OrganisationInvitationManager)
    }

    fun navigateToEditUser(organisationUser: OrganisationUser) = viewModelScope.launch {
        navigator.navigate(
            Destination.EditOrganisationUser(
                organisationUser.id,
                organisationUser.organisationUserName
            )
        )
    }

    fun navigateToAssignRole(organisationUser: OrganisationUser) = viewModelScope.launch {
        navigator.navigate(
            Destination.AssignRoleOrganisationUser(
                organisationUser.id,
                organisationUser.organisationRole
            )
        )
    }

    fun deleteOrganisationUser(organisationUserId: String) = viewModelScope.launch {
        organisationUserUseCases.deleteOrganisationUser.delete(organisationUserId)
            .collectLatest { response ->

                when (response) {
                    Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Error -> {
                        sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
    }

    fun cancelInviteByUserId(userId: String) = viewModelScope.launch {
        organisationUserUseCases.invitationUserUseCase.cancelInviteByUserId(userId).onEach {

        }.launchIn(viewModelScope)
    }

    fun cancelInviteByUserEmail(email: String) = viewModelScope.launch {
        organisationUserUseCases.invitationUserUseCase.cancelInviteByUserEmail(email)
            .onEach { response ->
                when (response) {
                    Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun inviteUser(organisationUser: OrganisationUser) {
        when (organisationUser.userId) {
            null -> inviteUserByEmail(organisationUser)
            else -> inviteUserByUserId(organisationUser)
        }
    }

    private fun inviteUserByEmail(organisationUser: OrganisationUser) = viewModelScope.launch {
        organisationUser.email?.let {
            organisationUserUseCases.invitationUserUseCase.inviteUserByEmail(
                OrganisationInvitationEmailRequest(
                    organisationUserName = organisationUser.organisationUserName,
                    organisationRole = organisationUser.organisationRole,
                    email = it
                )
            )
        }?.onEach {

        }?.launchIn(viewModelScope)
    }

    private fun inviteUserByUserId(organisationUser: OrganisationUser) = viewModelScope.launch {
        organisationUser.userId?.let {
            organisationUserUseCases.invitationUserUseCase.inviteUserByUserId(
                OrganisationInvitationUserIdRequest(
                    organisationUserName = organisationUser.organisationUserName,
                    organisationRole = organisationUser.organisationRole,
                    userId = organisationUser.userId
                )
            )
        }?.onEach {

        }?.launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        getOrganisationUsers()
    }


}
