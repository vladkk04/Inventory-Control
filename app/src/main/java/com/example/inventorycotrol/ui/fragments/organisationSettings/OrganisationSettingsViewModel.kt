package com.example.inventorycotrol.ui.fragments.organisationSettings

import OrganisationSettingsRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.usecase.organisatonSettings.OrganisationSettingsUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrganisationSettingsViewModel @Inject constructor(
    private val navigator: AppNavigator,
    private val organisationSettingsUseCases: OrganisationSettingsUseCases,
    private val dataStoreManager: DataStoreManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationSettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val notificationSettings = MutableStateFlow(uiState.value.notificationSettings)
    private var thresholdSettings = MutableStateFlow(uiState.value.thresholdSettings)

    init {
        organisationSettingsUseCases.get().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    _isLoading.update { true }
                }

                is Resource.Error -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    _isLoading.update { false }
                }

                is Resource.Success -> {
                    val data = result.data
                    _uiState.update {
                        it.copy(
                            notificationSettings = data.notificationSettings,
                            thresholdSettings = data.thresholdSettings
                        )
                    }
                    notificationSettings.update { data.notificationSettings }
                    thresholdSettings.update { data.thresholdSettings }

                    _isLoading.update { false }
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)

    }

    fun updateNotificationDays(notificationDays: Set<Int>) {
        notificationSettings.update { it.copy(notificationDays = notificationDays) }
    }

    fun updateNotificationTime(notificationTime: String) {
        notificationSettings.update { it.copy(notificationTime = notificationTime) }
    }

    fun updateNotifiableRoles(notifiableRoles: Set<OrganisationRole>) {
        notificationSettings.update { it.copy(notifiableRoles = notifiableRoles) }
    }

    fun updateNormalThreshold(normalThreshold: Double) = viewModelScope.launch {
        thresholdSettings.update { it.copy(normalThresholdPercentage = normalThreshold) }
    }

    fun updateMediumThreshold(mediumThreshold: Double) = viewModelScope.launch {
        thresholdSettings.update { it.copy(mediumThresholdPercentage = mediumThreshold) }
    }

    fun updateCriticalThreshold(criticalThreshold: Double) = viewModelScope.launch {
        thresholdSettings.update { it.copy(criticalThresholdPercentage = criticalThreshold) }
    }

    fun navigateBack() = viewModelScope.launch {
        navigator.navigateUp()
    }

    fun saveSettings() = viewModelScope.launch {
        val request = OrganisationSettingsRequest(
            notificationSettings = notificationSettings.value,
            thresholdSettings = thresholdSettings.value
        )

        organisationSettingsUseCases.update(request).onEach { result ->
            when (result) {
                ApiResponseResult.Loading -> {
                    _isLoading.update { true }
                }
                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    _isLoading.update { false }
                }
                is ApiResponseResult.Success -> {
                    _isLoading.update { false }
                    dataStoreManager.savePreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE to request.thresholdSettings.normalThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE to request.thresholdSettings.mediumThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE to request.thresholdSettings.criticalThresholdPercentage)
                    sendSnackbarEvent(SnackbarEvent("Settings saved successfully"))
                }
            }
        }.flowOn(dispatcher).launchIn(viewModelScope)
    }

}