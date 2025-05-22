package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.domain.model.organisation.settings.OrganisationSettingsRequest
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.OrganisationSettingsDto
import kotlinx.coroutines.flow.Flow

interface OrganisationSettingsRemoteDataSource {

    suspend fun get(): Flow<ApiResponseResult<OrganisationSettingsDto>>

    suspend fun update(request: OrganisationSettingsRequest): Flow<ApiResponseResult<Unit>>

}