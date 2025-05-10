package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class AssignRoleOrganisationUser(
    private val remote: OrganisationUserRemoteDataSource,
    private val local: OrganisationUserLocalDataSource,
) {
    operator fun invoke(
        organisationUserId: String,
        request: OrganisationUserAssignRoleRequest
    ) = performNetworkOperation(remoteCall = { remote.assignRole(organisationUserId, request) },
        localUpdate = { local.updateUserRole(organisationUserId, request.role) })
}