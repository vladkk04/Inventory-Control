package com.example.bachelorwork.data.remote.repositories.organisation

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.remote.dto.OrganisationDto
import com.example.bachelorwork.data.remote.services.OrganisationApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.organisation.OrganisationRequest
import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OrganisationRemoteDataSourceImpl(
    private val api: OrganisationApiService,
    private val dataStoreManager: DataStoreManager,
) : OrganisationRemoteDataSource {

    override suspend fun selectedOrganisationId(): String? =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()

    private suspend fun organisationId(): String? =
        dataStoreManager.getPreference(AppConstants.USER_PREFERRED_ORGANISATION_ID).firstOrNull()

    override suspend fun create(request: OrganisationRequest): Flow<ApiResponseResult<OrganisationDto>> {
        return safeResponseApiCallFlow {
            api.create(request).also {
                it.body()?.id?.let { organisationId ->
                    dataStoreManager.savePreference(AppConstants.SELECTED_ORGANISATION_ID to organisationId)
                    if (organisationId() == null) {
                        dataStoreManager.savePreference(AppConstants.USER_PREFERRED_ORGANISATION_ID to organisationId)
                    }
                }
            }
        }
    }

    override suspend fun getById(organisationId: String): Flow<ApiResponseResult<OrganisationDto>> =
        safeResponseApiCallFlow { api.get(organisationId) }

    override suspend fun get(): Flow<ApiResponseResult<OrganisationDto>> =
        safeResponseApiCallFlow { api.get(selectedOrganisationId() ?: "") }

    override suspend fun switchOrganisation(organisationId: String) {
        dataStoreManager.savePreference(AppConstants.SELECTED_ORGANISATION_ID to organisationId)
    }

    override suspend fun getAll(): Flow<ApiResponseResult<List<OrganisationDto>>> =
        safeResponseApiCallFlow { api.getAll() }

    override suspend fun leave(): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.leave(selectedOrganisationId() ?: "") }

    override suspend fun delete(): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.delete(selectedOrganisationId() ?: "") }

}