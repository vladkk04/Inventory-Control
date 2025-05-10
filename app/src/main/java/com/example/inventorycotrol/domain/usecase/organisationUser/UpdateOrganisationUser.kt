package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserUpdateRequest
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class UpdateOrganisationUser(
    private val remote: OrganisationUserRemoteDataSource,
    private val local: OrganisationUserLocalDataSource
) {
    operator fun invoke(
        organisationUserId: String,
        request: OrganisationUserUpdateRequest
    ) = performNetworkOperation(
        remoteCall = { remote.update(organisationUserId, request) },
        localUpdate = { local.updateUserName(organisationUserId, request.name) }
    )
}