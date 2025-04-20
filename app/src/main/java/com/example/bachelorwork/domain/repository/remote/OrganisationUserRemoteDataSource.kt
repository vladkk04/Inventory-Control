package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.OrganisationUserDto
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserUpdateRequest
import kotlinx.coroutines.flow.Flow

interface OrganisationUserRemoteDataSource {

    suspend fun getAll(): Flow<ApiResponseResult<List<OrganisationUserDto>>>

    suspend fun getByUserId(): Flow<ApiResponseResult<OrganisationUserDto>>

    suspend fun update(organisationUserId: String, request: OrganisationUserUpdateRequest): Flow<ApiResponseResult<Unit>>

    suspend fun assignRole(organisationUserId: String, request: OrganisationUserAssignRoleRequest): Flow<ApiResponseResult<Unit>>

    suspend fun delete(organisationUserId: String): Flow<ApiResponseResult<Unit>>

    suspend fun makeUserActive(organisationUserId: String): Flow<ApiResponseResult<Unit>>

    suspend fun makeUserInactive(organisationUserId: String): Flow<ApiResponseResult<Unit>>

    suspend fun cancelInviteByUserId(userId: String): Flow<ApiResponseResult<Unit>>

    suspend fun cancelInviteByUserEmail(email: String): Flow<ApiResponseResult<Unit>>

    suspend fun inviteUserByUserId(request: OrganisationInvitationUserIdRequest): Flow<ApiResponseResult<Unit>>

    suspend fun inviteUserByEmail(request: OrganisationInvitationEmailRequest): Flow<ApiResponseResult<Unit>>

}