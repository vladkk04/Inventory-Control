package com.example.bachelorwork.ui.fragments.organisationUsers.assignRole

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.bachelorwork.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
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
                ApiResponseResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is ApiResponseResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    navigator.navigateUp()
                }
            }
        }
    }

}