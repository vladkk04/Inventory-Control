package com.example.inventorycotrol.ui.fragments.organisation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.organisation.Organisation
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import com.example.inventorycotrol.ui.utils.FileMimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationEditViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUseCase: OrganisationUseCases,
    private val fileUseCases: FileUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrganisationEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(OrganisationEditFormState())
    val uiFormState = _uiFormState.asStateFlow()

    private val _canSaveChanges = MutableStateFlow(false)
    val canSaveChanges = _canSaveChanges.asStateFlow()

    private val _organisation = MutableStateFlow<Organisation?>(null)
    val organisation = _organisation.asStateFlow()

    init {
        viewModelScope.launch {
            getOrganisation()
        }
    }

    fun onEvent(event: OrganisationEditUiEvent) {
        when (event) {
            is OrganisationEditUiEvent.CurrencyChanged -> {
                _uiFormState.update { it.copy(currency = event.currency) }
                isInputCurrencyValidate()
            }

            is OrganisationEditUiEvent.DescriptionChanged -> {
                _uiFormState.update { it.copy(description = event.description) }
                isInputDescriptionValidate()
            }

            is OrganisationEditUiEvent.LogoChanged -> {
                _uiFormState.update { it.copy(logo = event.uri) }
                isInputLogoValidate()
            }

            is OrganisationEditUiEvent.OrganisationNameChanged -> {
                _uiFormState.update {
                    it.copy(organisationName = event.name, organisationError = null)
                }
                isInputFormOrganisationNameValidate()
            }

            OrganisationEditUiEvent.SaveChanges -> {
                viewModelScope.launch {
                    saveChanges()
                }
            }
        }
    }

    private suspend fun getOrganisation() {
        organisationUseCase.get.getOrganisation().onEach { result ->
            when (result) {
                Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Resource.Success -> {
                    _organisation.value = result.data
                    _uiFormState.update { it.copy(currency = result.data.currency) }
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun saveChanges() {
        val urlLogo = _uiFormState.value.logo?.let {
            fileUseCases.uploadFileUseCase.invoke(
                it,
                FileMimeType.PNG
            )
        }?.firstOrNull()

        organisation.value?.copy(
            logoUrl = urlLogo ?: organisation.value?.logoUrl,
            name = _uiFormState.value.organisationName,
            description = _uiFormState.value.description,
            currency = _uiFormState.value.currency
        )?.let { organisation ->
            organisationUseCase.update(organisation).onEach { result ->
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
                        navigateBack()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun isInputFormOrganisationNameValidate(): Boolean {
        val inputValidator = InputValidator
            .create()
            .withNotEmpty()
            .withMaxLength(40)
            .withTheSameValueEqual(organisation.value?.name ?: "")
            .build()
            .invoke(_uiFormState.value.organisationName)

        _canSaveChanges.value = !inputValidator.hasError

        return !inputValidator.hasError
    }

    private fun isInputDescriptionValidate(): Boolean {
        val inputValidator = InputValidator
            .create()
            .withMaxLength(250)
            .withTheSameValueEqual(organisation.value?.description ?: "")
            .build()
            .invoke(_uiFormState.value.description)

        _canSaveChanges.value = !inputValidator.hasError

        return !inputValidator.hasError
    }

    private fun isInputLogoValidate(): Boolean {
        val inputValidator = InputValidator
            .create()
            .withNotEmpty()
            .build()
            .invoke(_uiFormState.value.logo?.path.toString())

        _canSaveChanges.value = !inputValidator.hasError

        return !inputValidator.hasError
    }

    private fun isInputCurrencyValidate(): Boolean {
        val inputValidator = InputValidator
            .create()
            .withTheSameValueEqual(organisation.value?.currency ?: "")
            .build()
            .invoke(_uiFormState.value.currency)

        _canSaveChanges.value = !inputValidator.hasError

        return !inputValidator.hasError
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }
}