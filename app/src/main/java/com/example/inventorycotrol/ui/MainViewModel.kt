package com.example.inventorycotrol.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.organisation.OrganisationItem
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val organisationUseCase: OrganisationUseCases,
    private val navigator: AppNavigator,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _organisationRole = MutableStateFlow<OrganisationRole?>(null)
    val organisationRole = _organisationRole.asStateFlow()

    private val timeDelay = 2000L
    private var lastBackPressed = 0L

    init {
        viewModelScope.launch {
            dataStoreManager.getPreference(AppConstants.USER_ROLE).firstOrNull()?.let { gg ->
                _organisationRole.update {
                    OrganisationRole.valueOf(gg)
                }
            }
            dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).collectLatest { value ->
                value?.let {  _uiState.update { it.copy(selectedOrganisationId = value) } }
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            profileUseCases.getProfile.getProfile().onEach { result ->
                when (result) {
                    Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    }

                    is Resource.Success -> {
                        result.data?.organisationRole?.name?.let {
                            dataStoreManager.savePreference(AppConstants.USER_ROLE to it)
                        }
                        _organisationRole.update { result.data?.organisationRole }
                        _uiState.update {
                            it.copy(
                                profile = result.data,
                            )
                        }
                        getOrganisations()
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
        }
    }

    fun onBackPressed(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (lastBackPressed + timeDelay > currentTime) {
            true
        } else {
            sendSnackbarEvent(SnackbarEvent("Click exit again"))
            lastBackPressed = currentTime
            false
        }
    }

    fun switchOrganisation(organisationId: String) {
        organisationUseCase.switch.invoke(organisationId).onEach { result ->
            when (result) {
                Resource.Loading -> {

                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedOrganisationId = organisationId,
                            organisations = it.organisations.map { organisation ->
                                organisation.copy(
                                    isSelected = organisation.id == organisationId
                                )
                            }
                        )
                    }
                    navigateToHome()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getOrganisations() = viewModelScope.launch {
        organisationUseCase.get.invoke().onEach { result ->
            when (result) {
                Resource.Loading -> {

                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                }

                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            organisations = result.data.map {
                                OrganisationItem(
                                    id = it.id,
                                    name = it.name,
                                    currency = it.currency,
                                    description = it.description,
                                    logoUrl = it.logoUrl,
                                    createdBy = it.createdBy,
                                    createdAt = it.createdAt,
                                    isSelected = it.id == state.selectedOrganisationId
                                )
                            }
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToProfile() = viewModelScope.launch {
        navigator.navigate(Destination.Profile) {
            popUpTo(Destination.Home)
        }
    }

    private fun navigateToHome() = viewModelScope.launch {
        navigator.navigate(Destination.Home) {
            popUpTo(Destination.Home) {
                inclusive = true
                saveState = false
            }
            restoreState = false
        }
    }

    fun navigateToCreateOrganisation() = viewModelScope.launch {
        navigator.navigate(Destination.CreateOrganisation(true)) {
            launchSingleTop = true
            restoreState = true

            popUpTo(Destination.Home) {
                inclusive = false
                saveState = true
            }
        }
    }

}