package com.example.inventorycotrol.ui.fragments.organisationManage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
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
class OrganisationManageViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUseCases: OrganisationUseCases,
    private val authRepository: AuthRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationManageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            organisationUseCases.get.getOrganisation().collectLatest { result ->
                when(result) {
                    Resource.Loading -> {

                    }
                    is Resource.Error -> {

                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(organisation = result.data) }
                    }
                }
            }
        }
    }


    fun deleteOrganisation() = viewModelScope.launch {
        organisationUseCases.delete().onEach { response ->
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
        }.launchIn(viewModelScope)
    }

    fun navigateEditOrganisation() = viewModelScope.launch {
        navigator.navigate(Destination.OrganisationEdit)
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }




}