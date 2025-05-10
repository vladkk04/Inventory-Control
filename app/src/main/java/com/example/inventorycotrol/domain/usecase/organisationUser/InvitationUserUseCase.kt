package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation
import kotlinx.coroutines.flow.Flow

class InvitationUserUseCase(
    private val repository: OrganisationUserRemoteDataSource,
    private val local: OrganisationUserLocalDataSource,
) {
    fun inviteUserByUserId(request: OrganisationInvitationUserIdRequest): Flow<Resource<Unit>> =
        performNetworkOperation(
            remoteCall = { repository.inviteUserByUserId(request) },
            localUpdate = { local.insert(it.mapToEntity()) },
        )

    fun inviteUserByEmail(request: OrganisationInvitationEmailRequest): Flow<Resource<Unit>> =
        performNetworkOperation(
            remoteCall = { repository.inviteUserByEmail(request) },
            localUpdate = { local.insert(it.mapToEntity()) },
        )

     fun cancelInviteByUserId(userId: String): Flow<Resource<Unit>> =
        performNetworkOperation(
            remoteCall = { repository.cancelInviteByUserId(userId) },
            localUpdate = { local.deleteByUserId(userId) },
        )


    fun cancelInviteByUserEmail(email: String): Flow<Resource<Unit>> =
        performNetworkOperation(
            remoteCall = {  repository.cancelInviteByUserEmail(email) },
            localUpdate = { local.deleteByEmail(email) },
        )

}
