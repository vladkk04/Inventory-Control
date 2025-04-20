package com.example.bachelorwork.ui.fragments.organisationUsers.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserUpdateRequest
import com.example.bachelorwork.domain.model.validator.InputValidator
import com.example.bachelorwork.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
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
                ApiResponseResult.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is ApiResponseResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    sendSnackbarEvent(SnackbarEvent(response.errorMessage))
                }
                is ApiResponseResult.Success -> {
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