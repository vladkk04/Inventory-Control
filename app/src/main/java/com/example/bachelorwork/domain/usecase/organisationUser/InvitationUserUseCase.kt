package com.example.bachelorwork.domain.usecase.organisationUser

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class InvitationUserUseCase(
    private val repository: OrganisationUserRemoteDataSource
) {
    suspend fun inviteUserByUserId(request: OrganisationInvitationUserIdRequest): Flow<ApiResponseResult<Unit>> = repository.inviteUserByUserId(request)

    suspend fun inviteUserByEmail(request: OrganisationInvitationEmailRequest): Flow<ApiResponseResult<Unit>> = repository.inviteUserByEmail(request)

    suspend fun cancelInviteByUserId(userId: String): Flow<ApiResponseResult<Unit>> = repository.cancelInviteByUserId(userId)

    suspend fun cancelInviteByUserEmail(email: String): Flow<ApiResponseResult<Unit>> = repository.cancelInviteByUserEmail(email)
}
