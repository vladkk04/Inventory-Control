package com.example.inventorycotrol.ui.fragments.organisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
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
class OrganisationListViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUseCases: OrganisationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllOrganisationsByUser()
    }

    private fun getAllOrganisationsByUser() = viewModelScope.launch {
        organisationUseCases.get.invoke().distinctUntilChanged().collectLatest { result  ->
            when (result) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(organisations = emptyList(), isLoading = false, isRefreshing = false) }
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false, organisations = result.data) }
                }
            }
        }
    }

    fun switchOrganisation(organisationId: String) = viewModelScope.launch {
        organisationUseCases.switch.invoke(organisationId).distinctUntilChanged().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }
                is Resource.Success -> {
                    navigator.navigate(Destination.Home) {
                        popUpTo(Destination.InvitationListAfterSignUp) {
                            inclusive = true
                            saveState = false
                        }
                        restoreState = false
                    }
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        getAllOrganisationsByUser()
    }

}