package com.example.bachelorwork.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.domain.usecase.organisation.OrganisationUseCases
import com.example.bachelorwork.domain.usecase.profile.ProfileUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val organisationUseCase: OrganisationUseCases,
    private val userUseCases: ProfileUseCases,
    private val navigator: AppNavigator
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userUseCases.getProfile.getProfile().onEach { result ->
                when (result) {
                    Resource.Loading -> {

                    }
                    is Resource.Error -> {

                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(profile = result.data)
                        }
                        getOrganisations()
                        
                        Log.d("debug", result.data.toString())
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private val timeDelay = 2000L
    private var lastBackPressed = 0L


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

    }

    private fun getOrganisations() = viewModelScope.launch {
        organisationUseCase.get.invoke().collectLatest { result ->
            when (result) {
                Resource.Loading -> {

                }

                is Resource.Error -> {

                }

                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedOrganisationId = organisationUseCase.get.getOrganisationId(),
                            organisations = result.data
                        )
                    }
                }
            }
        }
    }

    fun navigateToProfile() = viewModelScope.launch {
        navigator.navigate(Destination.Profile)
    }

}