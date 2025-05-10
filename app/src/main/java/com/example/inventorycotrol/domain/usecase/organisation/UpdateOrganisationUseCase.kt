package com.example.inventorycotrol.domain.usecase.organisation

import com.example.inventorycotrol.data.local.mappers.mapToEntity
import com.example.inventorycotrol.data.remote.mappers.mapToRequest
import com.example.inventorycotrol.domain.model.organisation.Organisation
import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class UpdateOrganisationUseCase(
    private val remote: OrganisationRemoteDataSource,
    private val local: OrganisationLocalDataSource
) {
    operator fun invoke(organisation: Organisation) = performNetworkOperation(
        remoteCall = { remote.update(organisation.mapToRequest()) },
        localUpdate = { local.update(organisation.mapToEntity()) }
    )
}