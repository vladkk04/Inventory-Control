package com.example.inventorycotrol.data.remote.repositories

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.OrganisationInvitationDto
import com.example.inventorycotrol.data.remote.dto.UserDto
import com.example.inventorycotrol.data.remote.services.ProfileApiService
import com.example.inventorycotrol.domain.model.auth.ChangeEmailRequest
import com.example.inventorycotrol.domain.model.auth.ChangePasswordRequest
import com.example.inventorycotrol.domain.model.profile.ChangeInfoUserRequest
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeResponseApiCallFlow
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

    override suspend fun changePassword(request: ChangePasswordRequest): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.changePassword(request) }

    override suspend fun changeInfoUser(request: ChangeInfoUserRequest): Flow<ApiResponseResult<UserDto>> =
        safeResponseApiCallFlow { api.changeInfoUser(request) }


    override suspend fun changeEmail(request: ChangeEmailRequest): Flow<ApiResponseResult<UserDto>> =
        safeResponseApiCallFlow { api.changeEmail(request) }

}