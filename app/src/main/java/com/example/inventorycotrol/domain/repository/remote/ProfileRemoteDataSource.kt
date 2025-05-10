package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.OrganisationInvitationDto
import com.example.inventorycotrol.data.remote.dto.UserDto
import com.example.inventorycotrol.domain.model.auth.ChangeEmailRequest
import com.example.inventorycotrol.domain.model.auth.ChangePasswordRequest
import com.example.inventorycotrol.domain.model.profile.ChangeInfoUserRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProfileRemoteDataSource {

    suspend fun getOrganisationsInviting(): Response<List<OrganisationInvitationDto>>

    suspend fun acceptOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>>

    suspend fun changeEmail(request: ChangeEmailRequest): Flow<ApiResponseResult<UserDto>>

    suspend fun changePassword(request: ChangePasswordRequest): Flow<ApiResponseResult<Unit>>

    suspend fun changeInfoUser(request: ChangeInfoUserRequest): Flow<ApiResponseResult<UserDto>>

    suspend fun declineOrganisationInvitation(id: String): Flow<ApiResponseResult<Unit>>
}