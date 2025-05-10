package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class DeleteOrganisationUser(
    private val organisationUserRemoteDataSource: OrganisationUserRemoteDataSource,
    private val organisationUserLocalDataSource: OrganisationUserLocalDataSource,
) {
    fun delete(organisationUserId: String) =
        performNetworkOperation(
            remoteCall = {
                organisationUserRemoteDataSource.delete(organisationUserId)
            },
            localUpdate = { organisationUserLocalDataSource.deleteById(organisationUserId) }
        )
}