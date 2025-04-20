package com.example.bachelorwork.ui.fragments.organisationInvitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.usecase.profile.ProfileUseCases
import com.example.bachelorwork.ui.model.organisationInvitations.InvitationListUiState
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InvitationListViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val profileUseCases: ProfileUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationListUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getInvitations()
    }

    private fun getInvitations() = viewModelScope.launch {
        profileUseCases.getOrganisationsInviting().collectLatest { response ->
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
                            invitations = response.data,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun acceptInvitation(id: String) = viewModelScope.launch {
        profileUseCases.acceptOrganisationInvitation.invoke(id).collectLatest { response ->
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

    fun declineInvitation(id: String) = viewModelScope.launch {
        profileUseCases.declineOrganisationInvitation.invoke(id).collectLatest { response ->
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

    fun refreshInvitations() {
        _uiState.update { it.copy(isRefreshing = true) }
        getInvitations()
    }
}