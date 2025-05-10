package com.example.inventorycotrol.ui.fragments.changeEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangeEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")

    val emailError = MutableStateFlow<String?>(null)

    fun setupChangeEmail(email: String) {
        _email.value = email
        isEmailValid()
    }


    private fun isEmailValid(): Boolean {
        val inputValidator = InputValidator.create()
            .withNotEmpty()
            .withEmail()
            .build()
            .invoke(_email.value)

        emailError.value = inputValidator.errorMessage

        return !inputValidator.hasError
    }

    fun changeEmail() = viewModelScope.launch {
        profileUseCases.changeEmail(_email.value).onEach { result ->
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

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

}