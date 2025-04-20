package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.OrganisationDto
import com.example.bachelorwork.domain.model.organisation.OrganisationRequest
import kotlinx.coroutines.flow.Flow

interface OrganisationRemoteDataSource {

    suspend fun selectedOrganisationId(): String?

    suspend fun create(request: OrganisationRequest): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun getById(organisationId: String): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun getAll(): Flow<ApiResponseResult<List<OrganisationDto>>>

    suspend fun get(): Flow<ApiResponseResult<OrganisationDto>>

    suspend fun switchOrganisation(organisationId: String)

    suspend fun leave(): Flow<ApiResponseResult<Unit>>

    suspend fun delete(): Flow<ApiResponseResult<Unit>>

}