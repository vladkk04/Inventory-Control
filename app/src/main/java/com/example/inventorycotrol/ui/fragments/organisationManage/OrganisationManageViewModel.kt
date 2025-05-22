package com.example.inventorycotrol.ui.fragments.organisationManage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationManageViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUseCases: OrganisationUseCases,
    private val authRepository: AuthRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationManageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            organisationUseCases.get.getOrganisation().distinctUntilChanged().collectLatest { result ->
                when(result) {
                    Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(organisation = result.data) }
                        sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(organisation = result.data) }
                    }
                }
            }
        }
    }

    fun navigateEditOrganisation() = viewModelScope.launch {
        navigator.navigate(Destination.EditOrganisation)
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }




}