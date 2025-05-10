package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.OrganisationDto
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest
import kotlinx.coroutines.flow.Flow

interface OrganisationRemoteDataSource {

    suspend fun selectedOrganisationId(): String?

    suspend fun getById(organisationId: String): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<OrganisationDto>>>

    suspend fun get(): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun create(request: OrganisationRequest): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun update(request: OrganisationRequest): Flow<ApiResponseResult<Unit>>

    suspend fun switchOrganisation(organisationId: String)

    suspend fun leave(): Flow<ApiResponseResult<Unit>>

    suspend fun delete(): Flow<ApiResponseResult<Unit>>

}