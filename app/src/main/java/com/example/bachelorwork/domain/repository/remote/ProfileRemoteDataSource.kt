package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.data.remote.dto.OrganisationInvitationDto
import com.example.bachelorwork.common.ApiResponseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProfileRemoteDataSource {

    suspend fun getOrganisationsInviting(): Response<List<OrganisationInvitationDto>>

    suspend fun acceptOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>>

    suspend fun declineOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>>
}