package com.example.inventorycotrol.ui.fragments.organisationSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.di.IoDispatcher
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.settings.OrganisationSettingsRequest
import com.example.inventorycotrol.domain.usecase.organisatonSettings.OrganisationSettingsUseCases
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.inventorycotrol.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
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

    val notificationSettings = MutableStateFlow(uiState.value.notificationSettings)
    var thresholdSettings = MutableStateFlow(uiState.value.thresholdSettings)


    init {
        organisationSettingsUseCases.get().distinctUntilChanged().onEach { result ->
            when (result) {
                Resource.Loading -> {
                    _isLoading.update { true }
                }

                is Resource.Error -> {
                    val notifyOnly = dataStoreManager.getPreference(AppConstants.NOTIFY_ONLY).firstOrNull()
                    val notificationTime = dataStoreManager.getPreference(AppConstants.NOTIFICATION_TIME).firstOrNull()
                    val notificationDays = dataStoreManager.getPreference(AppConstants.NOTIFICATION_DAYS).firstOrNull()

                    val normal = dataStoreManager.getPreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE).firstOrNull()
                    val medium = dataStoreManager.getPreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE).firstOrNull()
                    val critical = dataStoreManager.getPreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE).firstOrNull()

                    notificationSettings.update {
                        it.copy(
                            notificationTime = notificationTime ?: "07:00",
                            notificationDays = notificationDays?.map { day -> day.toInt() }?.toSet() ?: emptySet(),
                            notifiableRoles = notifyOnly?.map { role -> OrganisationRole.valueOf(role) }?.toSet() ?: emptySet()
                        )
                    }

                    val thresholdSettingss = ThresholdSettings(
                        normalThresholdPercentage = normal ?: 0.0,
                        mediumThresholdPercentage = medium ?: 0.0,
                        criticalThresholdPercentage = critical ?: 0.0
                    )

                    thresholdSettings.update { thresholdSettingss }

                    _uiState.update {
                        it.copy(
                            notificationSettings = notificationSettings.value,
                            thresholdSettings = thresholdSettingss
                        )
                    }


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

    fun saveSettings(isSaved: (Boolean) -> Unit) = viewModelScope.launch {
        if(!inputValidation()) return@launch

        val request = OrganisationSettingsRequest(
            notificationSettings = notificationSettings.value,
            thresholdSettings = thresholdSettings.value
        )

        organisationSettingsUseCases.update(request).distinctUntilChanged().onEach { result ->
            when (result) {
                ApiResponseResult.Loading -> {
                    _isLoading.update { true }
                }
                is ApiResponseResult.Failure -> {
                    sendSnackbarEvent(SnackbarEvent(result.errorMessage))
                    isSaved(false)
                    _isLoading.update { false }
                }
                is ApiResponseResult.Success -> {
                    _isLoading.update { false }
                    dataStoreManager.savePreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE to request.thresholdSettings.normalThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE to request.thresholdSettings.mediumThresholdPercentage)
                    dataStoreManager.savePreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE to request.thresholdSettings.criticalThresholdPercentage)
                    isSaved(true)
                    sendSnackbarEvent(SnackbarEvent("Settings saved successfully"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun inputValidation(): Boolean {
        var result = true

        with(thresholdSettings.value) {
            if(normalThresholdPercentage <= mediumThresholdPercentage) {
                sendSnackbarEvent(SnackbarEvent("Normal threshold must be more than medium threshold"))
                result = false
            }
            if(mediumThresholdPercentage <= criticalThresholdPercentage) {
                sendSnackbarEvent(SnackbarEvent("Medium threshold must be more than critical threshold"))
                result = false
            }
            if(criticalThresholdPercentage == 0.00) {
                sendSnackbarEvent(SnackbarEvent("Critical threshold must not be zero"))
                result = false
            }
        }

        return result
    }

}