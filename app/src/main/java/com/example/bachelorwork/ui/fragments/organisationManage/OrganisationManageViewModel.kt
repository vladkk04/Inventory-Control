package com.example.bachelorwork.ui.fragments.organisationManage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.entities.mapToDomain
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.domain.usecase.organisation.OrganisationUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

            organisationUseCases.get.getOrganisation().onEach { result ->
                when(result) {
                    Resource.Loading -> {

                    }
                    is Resource.Error -> {

                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(organisation = result.data.mapToDomain()) }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    fun deleteOrganisation() = viewModelScope.launch {
        /*organisationUseCases.deleteOrganisationUseCase().collectLatest { response ->
            when(response) {
                ApiResponseResult.Loading -> {

                }
                is ApiResponseResult.Failure -> {

                }
                is ApiResponseResult.Success -> {
                    navigator.navigate(Destination.InvitationListAfterSignUp) {
                        popUpTo(Destination.Home) {
                            inclusive = true
                        }
                    }
                }
            }
        }*/
    }

}