package com.example.inventorycotrol.data.remote.repositories.organisation

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.OrganisationDto
import com.example.inventorycotrol.data.remote.services.OrganisationApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OrganisationRemoteDataSourceImpl(
    private val api: OrganisationApiService,
    private val dataStoreManager: DataStoreManager,
) : OrganisationRemoteDataSource {

    override suspend fun selectedOrganisationId(): String? =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()


    override suspend fun create(request: OrganisationRequest): Flow<ApiResponseResult<OrganisationDto>> {
        return safeResponseApiCallFlow {
            api.create(request).also { request ->
                request.body()?.let { organisationId ->
                    dataStoreManager.savePreference(AppConstants.SELECTED_ORGANISATION_ID to organisationId.id)
                    dataStoreManager.savePreference(AppConstants.ORGANISATION_CURRENCY to organisationId.currency)
                }
            }
        }
    }

    override suspend fun update(
        request: OrganisationRequest
    ): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.update(selectedOrganisationId() ?: "", request) }
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