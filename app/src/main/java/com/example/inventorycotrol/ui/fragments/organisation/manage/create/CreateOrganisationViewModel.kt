package com.example.inventorycotrol.ui.fragments.organisation.manage.create

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.ui.model.organisation.CreateOrganisationUiEvent
import com.example.inventorycotrol.ui.model.organisation.CreateOrganisationUiFormState
import com.example.inventorycotrol.ui.model.organisation.CreateOrganisationUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import com.example.inventorycotrol.domain.model.file.FileMimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrganisationViewModel @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val navigator: AppNavigator,
    private val organisationUseCases: OrganisationUseCases,
    private val fileUseCases: FileUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOrganisationUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiStateForm = MutableStateFlow(CreateOrganisationUiFormState())
    val uiStateFrom get() = _uiStateForm.asStateFlow()

    private val logo = MutableStateFlow<Uri?>(null)

    private val destArg = Destination.from<Destination.CreateOrganisation>(savedStateHandle)

    fun onEvent(event: CreateOrganisationUiEvent) {
        when (event) {
            is CreateOrganisationUiEvent.OrganisationNameChanged -> {
                _uiStateForm.update {
                    it.copy(
                        organisationName = event.name.trim(),
                        organisationError = null
                    )
                }
            }

            is CreateOrganisationUiEvent.CurrencyChanged -> {
                _uiStateForm.update {
                    it.copy(
                        currency = event.currency,
                        currencyError = null
                    )
                }
            }

            is CreateOrganisationUiEvent.DescriptionChanged -> {
                _uiStateForm.update {
                    it.copy(
                        description = event.description.trim()
                    )
                }
            }

            CreateOrganisationUiEvent.Create -> {
                createOrganisation()
            }

            is CreateOrganisationUiEvent.LogoChanged -> {
                logo.value = event.uri
            }
        }
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    private fun createOrganisation() = viewModelScope.launch {

        if(!isValidInputs()) {
            return@launch
        }


        _uiState.update { it.copy(isLoading = true) }

        val urlLogo = logo.value?.let {
            fileUseCases.uploadFileUseCase.invoke(
                it,
                FileMimeType.PNG
            )
        }?.firstOrNull()

        organisationUseCases.create(
            OrganisationRequest(
                name = _uiStateForm.value.organisationName,
                description = _uiStateForm.value.description,
                currency = _uiStateForm.value.currency,
                logoUrl = urlLogo
            )
        ).distinctUntilChanged().onEach { response ->
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
                    navigator.navigate(Destination.Home) {
                        if (destArg.isNavigatedFromSidePanel) {
                            popUpTo(Destination.Home) {
                                inclusive = true
                                saveState = false
                            }
                        } else {
                            popUpTo(Destination.InvitationListAfterSignUp) {
                                inclusive = true
                                saveState = false
                            }
                        }
                        restoreState = false
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun isValidInputs(): Boolean {

        val inputValidator = InputValidator.create()
            .withNotEmpty()
            .build()

        val organisationInputValidator = InputValidator.create()
            .withNotEmpty()
            .withMaxLength(40)
            .build()

        val organisationName = organisationInputValidator.invoke(_uiStateForm.value.organisationName)
        val currency = inputValidator.invoke(_uiStateForm.value.currency)

        _uiStateForm.update {
            it.copy(
                organisationError = organisationName.errorMessage,
                currencyError = currency.errorMessage
            )
        }

        return !(organisationName.hasError || currency.hasError)
    }
}