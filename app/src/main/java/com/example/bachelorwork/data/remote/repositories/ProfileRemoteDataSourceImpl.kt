package com.example.bachelorwork.data.remote.repositories

import com.example.bachelorwork.data.remote.dto.OrganisationInvitationDto
import com.example.bachelorwork.data.remote.services.ProfileApiService
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.repository.remote.ProfileRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ProfileRemoteDataSourceImpl(
    private val api: ProfileApiService,
): ProfileRemoteDataSource {

    override suspend fun getOrganisationsInviting(): Response<List<OrganisationInvitationDto>> {
        return api.getOrganisationsInviting()
    }

    override suspend fun acceptOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.acceptOrganisationInvitation(id) }
    }

    override suspend fun declineOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.declineOrganisationInvitation(id) }
    }
}