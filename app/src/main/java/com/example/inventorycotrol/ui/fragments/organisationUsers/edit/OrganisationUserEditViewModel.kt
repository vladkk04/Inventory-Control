package com.example.inventorycotrol.ui.fragments.organisationUsers.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserUpdateRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationUserEditViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle,
    private val organisationUserUseCases: OrganisationUserUseCases
) : ViewModel() {

    private val navigationData =
        Destination.from<Destination.EditOrganisationUser>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrganisationUserEditUiState())
    val uiState = _uiState.asStateFlow()

    private val organisationUserName = MutableStateFlow(navigationData.name)
    val organisationUserNameState = organisationUserName.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            organisationUserName = navigationData.name
        )
    }

    fun changeOrganisationUserName(name: String) {
        organisationUserName.value = name

        isValidateInput()
    }

    fun saveUpdates() = viewModelScope.launch {
        organisationUserUseCases.updateOrganisationUser(
            navigationData.organisationUserId, OrganisationUserUpdateRequest(
                name = organisationUserName.value
            )
        ).collectLatest { response ->
            when (response) {
                Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigator.navigateUp()
                }
            }
        }
    }


    private fun isValidateInput(): Boolean {
        val inputValidator = InputValidator.create()
            .withNotEmpty()
            .build()
            .invoke(organisationUserName.value)

        _uiState.value = _uiState.value.copy(
            organisationUserNameError = inputValidator.errorMessage
        )

        return !inputValidator.hasError
    }

}