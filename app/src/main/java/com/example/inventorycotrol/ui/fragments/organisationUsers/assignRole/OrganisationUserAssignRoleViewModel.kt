package com.example.inventorycotrol.ui.fragments.organisationUsers.assignRole

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationUserAssignRoleViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle,
    private val organisationUserUseCases: OrganisationUserUseCases
) : ViewModel() {

    private val navigationData =
        Destination.from<Destination.AssignRoleOrganisationUser>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrganisationUserAssignRoleUiState())
    val uiState = _uiState.asStateFlow()

    private val _organisationRole = MutableStateFlow(navigationData.organisationRole)
    val organisationRole get() = _organisationRole.asStateFlow()


    fun changeOrganisationRole(organisationRole: OrganisationRole) {
        _organisationRole.value = organisationRole
    }

    fun assignRole() = viewModelScope.launch {
        if (_organisationRole.value == navigationData.organisationRole) {
            navigator.navigateUp()
            return@launch
        }

        organisationUserUseCases.assignRoleOrganisationUser.invoke(
            navigationData.organisationUserId, OrganisationUserAssignRoleRequest(
                _organisationRole.value
            )
        ).collect { result ->
            when (result) {
                Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    navigator.navigateUp()
                }
            }
        }
    }

}