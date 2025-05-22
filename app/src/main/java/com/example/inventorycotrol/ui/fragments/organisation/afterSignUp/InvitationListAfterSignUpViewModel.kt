package com.example.inventorycotrol.ui.fragments.organisation.afterSignUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.StatusInvitation
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InvitationListAfterSignUpViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val profileUseCases: ProfileUseCases,
    private val organisationRepository: OrganisationUseCases,
    private val authRepository: AuthRemoteDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationListAfterSignUpUiState())
    val uiState = _uiState.asStateFlow()

    init { getOrganisationsInviting() }

    private fun getOrganisationsInviting() = viewModelScope.launch {
        profileUseCases.getOrganisationsInviting().distinctUntilChanged().collectLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is ApiResponseResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                }

                is ApiResponseResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            invitations = response.data.filter { it.status == StatusInvitation.PENDING },
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    fun navigateToCreateOrganisation() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrganisation())
    }

    fun navigateToOrganisationList() = viewModelScope.launch {
        navigator.navigate(Destination.OrganisationList)
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut().distinctUntilChanged().collectLatest { response ->

            when (response) {
                ApiResponseResult.Loading -> {

                }
                is ApiResponseResult.Failure -> {

                }
                is ApiResponseResult.Success -> {
                    navigator.navigate(Destination.Authentication) {
                        popUpTo(Destination.InvitationListAfterSignUp) {
                            inclusive = true
                            saveState = false
                        }
                        restoreState = false
                    }
                }
            }
        }
    }

    fun acceptInvitation(id: String) = viewModelScope.launch {
        profileUseCases.acceptOrganisationInvitation(id).distinctUntilChanged().onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent("Something went wrong"))
                }
                is ApiResponseResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            invitations = state.invitations - state.invitations.first { it.id == id },
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent("Invitation accepted"))
                }
            }
        }.launchIn(viewModelScope)


    }

    fun declineInvitation(id: String) = viewModelScope.launch {
        profileUseCases.declineOrganisationInvitation(id).distinctUntilChanged().onEach { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent("Something went wrong"))
                }
                is ApiResponseResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            invitations = state.invitations - state.invitations.first { it.id == id },
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent("Invitation declined"))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refreshInvitations() {
        getOrganisationsInviting()
        _uiState.update { it.copy(isRefreshing = true) }
    }


}