package com.example.inventorycotrol.ui.fragments.profile.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.profile.ChangeInfoUserRequest
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
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
class ProfileEditViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val fileUseCases: FileUseCases,
    private val profileUseCases: ProfileUseCases,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val destinationArg = Destination.from<Destination.ProfileEdit>(savedStateHandle)

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiFormState = MutableStateFlow(ProfileEditFormState())
    val uiFormState = _uiFormState.asStateFlow()

    private val _canSaveChanges = MutableStateFlow(false)
    val canSaveChanges = _canSaveChanges.asStateFlow()


    init {
        _uiState.update {
            it.copy(
                fullName = destinationArg.fullName,
                logoUrl = destinationArg.logo
            )
        }
    }

    fun onEvent(event: ProfileEditUiEvent) {
        when (event) {
            is ProfileEditUiEvent.FullNameChanged -> {
                _uiFormState.update {
                    it.copy(fullName = event.name.trim(), fullNameError = null)
                }
                isInputFullNameValidate()
            }

            is ProfileEditUiEvent.LogoChanged -> {
                _uiFormState.update { it.copy(logo = event.uri) }
                isInputLogoValidate()
            }

            ProfileEditUiEvent.SaveChanges -> {
                saveChanges()
            }
        }
    }

    private fun saveChanges() = viewModelScope.launch {
        val url = _uiFormState.value.logo?.let {
            fileUseCases.uploadFileUseCase.invoke(
                it,
                FileMimeType.PNG
            ).firstOrNull()
        }


        val info = ChangeInfoUserRequest(
            fullName = _uiFormState.value.fullName.ifBlank { uiState.value.fullName },
            logoUrl = url ?: uiState.value.logoUrl
        )

        profileUseCases.changeUserInfo.invoke(info).onEach { result ->
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

    private fun isInputFullNameValidate(): Boolean {
        val inputValidator = InputValidator
            .create()
            .withMaxLength(50)
            .withTheSameValueEqual(uiState.value.fullName)
            .build()
            .invoke(_uiFormState.value.fullName)

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

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }
}