package com.example.bachelorwork.ui.fragments.organisationUsers.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUser
import com.example.bachelorwork.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.bachelorwork.ui.model.organisationUser.OrganisationUserListUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
        viewModelScope.launch {
            organisationUserUseCases.getOrganisationUsersUseCase().collectLatest { response ->
                when (response) {
                    Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {

                        val ownId = dataStoreManager.getPreference(AppConstants.USER_ID_KEY).first()

                        _uiState.update {
                            it.copy(
                                organisationUsers = response.data.sortedByDescending { user -> user.userId == ownId },
                                ownId = ownId
                            )
                        }
                    }
                }
            }
        }
    }

    fun navigateToManageUser() = viewModelScope.launch {
        navigator.navigate(Destination.OrganisationManageUser)
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
                    ApiResponseResult.Loading -> {

                    }

                    is ApiResponseResult.Failure -> {

                    }

                    is ApiResponseResult.Success -> {

                    }

                }
            }
    }

    fun makeUserInactive(organisationUserId: String) = viewModelScope.launch {
        organisationUserUseCases.chaneStatusOrganisationUserUseCase.makeUserInactive(
            organisationUserId
        ).collectLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {

                }

                is ApiResponseResult.Success -> {

                }
            }
        }
    }

    fun makeUserActive(organisationUserId: String) = viewModelScope.launch {
        organisationUserUseCases.chaneStatusOrganisationUserUseCase.makeUserActive(
            organisationUserId
        ).collectLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> {

                }

                is ApiResponseResult.Failure -> {

                }

                is ApiResponseResult.Success -> {

                }
            }
        }
    }

    fun cancelInviteByUserId(userId: String) = viewModelScope.launch {
        organisationUserUseCases.invitationUserUseCase.cancelInviteByUserId(userId)
            .collectLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> {

                    }

                    is ApiResponseResult.Failure -> {

                    }

                    is ApiResponseResult.Success -> {

                    }
                }
            }
    }

    fun cancelInviteByUserEmail(email: String) = viewModelScope.launch {
        organisationUserUseCases.invitationUserUseCase.cancelInviteByUserEmail(email)
            .collectLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> {

                    }

                    is ApiResponseResult.Failure -> {

                    }

                    is ApiResponseResult.Success -> {

                    }
                }
            }
    }

    fun inviteUser(organisationUser: OrganisationUser) {
        when (organisationUser.userId) {
            null -> inviteUserByEmail(organisationUser)
            else -> inviteUserByUserId(organisationUser)
        }
    }

    private fun inviteUserByEmail(organisationUser: OrganisationUser) = viewModelScope.launch {

        Log.d("debug", organisationUser.toString())

        organisationUser.email?.let {
            organisationUserUseCases.invitationUserUseCase.inviteUserByEmail(
                OrganisationInvitationEmailRequest(
                    organisationUserName = organisationUser.organisationUserName,
                    organisationRole = organisationUser.organisationRole,
                    email = it
                )
            )
        }?.collect {
            when (it) {
                ApiResponseResult.Loading -> {}
                is ApiResponseResult.Failure -> {
                    Log.d("debug", it.errorMessage)
                }

                is ApiResponseResult.Success -> {}
            }
        }
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
        }?.collect {
            when (it) {
                ApiResponseResult.Loading -> {}
                is ApiResponseResult.Failure -> {}
                is ApiResponseResult.Success -> {}
            }
        }
    }


}
