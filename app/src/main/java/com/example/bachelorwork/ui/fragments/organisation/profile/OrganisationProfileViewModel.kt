package com.example.bachelorwork.ui.fragments.organisation.profile

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.domain.usecase.organisation.OrganisationUseCases
import com.example.bachelorwork.ui.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrganisationProfileViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationUseCases: OrganisationUseCases,
): ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationProfileUiState())
    val uiState = _uiState.asStateFlow()


    init {

    }



}