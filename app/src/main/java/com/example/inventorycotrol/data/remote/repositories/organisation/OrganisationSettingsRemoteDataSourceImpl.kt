package com.example.inventorycotrol.data.remote.repositories.organisation

import com.example.inventorycotrol.domain.model.organisation.settings.OrganisationSettingsRequest
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.OrganisationSettingsDto
import com.example.inventorycotrol.data.remote.services.OrganisationSettingsApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.remote.OrganisationSettingsRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeSuspendResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class OrganisationSettingsRemoteDataSourceImpl(
    private val api: OrganisationSettingsApiService,
    private val dataStore: DataStoreManager
) : OrganisationSettingsRemoteDataSource {

    private suspend fun organisationId() =
        dataStore.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw Exception("No organisation selected")

    override suspend fun get(): Flow<ApiResponseResult<OrganisationSettingsDto>> {
        return safeSuspendResponseApiCallFlow { api.get(organisationId()) }.also {
            when (val response = it.first()) {
                ApiResponseResult.Loading -> {}
                is ApiResponseResult.Failure -> {}

                is ApiResponseResult.Success -> {
                    val data = response.data
                    val notificationDays =
                        data.notificationSettings.notificationDays.map { value -> value.toString() }
                            .toSet()

                    dataStore.updatePreference(AppConstants.NOTIFY_ONLY to data.notificationSettings.notifiableRoles.map { g -> g.name }
                        .toSet())
                        .exceptionOrNull()

                    dataStore.updatePreference(AppConstants.NOTIFICATION_TIME to data.notificationSettings.notificationTime)
                        .exceptionOrNull()
                    dataStore.updatePreference(AppConstants.NOTIFICATION_DAYS to notificationDays)
                        .exceptionOrNull()
                    dataStore.updatePreference(AppConstants.NORMAL_THRESHOLD_PERCENTAGE to data.thresholdSettings.normalThresholdPercentage)
                        .exceptionOrNull()
                    dataStore.updatePreference(AppConstants.MEDIUM_THRESHOLD_PERCENTAGE to data.thresholdSettings.mediumThresholdPercentage)
                        .exceptionOrNull()
                    dataStore.updatePreference(AppConstants.CRITICAL_THRESHOLD_PERCENTAGE to data.thresholdSettings.criticalThresholdPercentage)
                        .exceptionOrNull()
                }
            }
        }
    }


    override suspend fun update(request: OrganisationSettingsRequest): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.update(organisationId(), request) }
    }
}