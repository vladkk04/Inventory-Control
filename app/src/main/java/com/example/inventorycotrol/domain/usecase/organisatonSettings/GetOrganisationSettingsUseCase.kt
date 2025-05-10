package com.example.inventorycotrol.domain.usecase.organisatonSettings

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.domain.model.organisation.settings.OrganisationSettings
import com.example.inventorycotrol.domain.repository.remote.OrganisationSettingsRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation
import kotlinx.coroutines.flow.Flow

class GetOrganisationSettingsUseCase(
    private val remote: OrganisationSettingsRemoteDataSource,
) {
    operator fun invoke(): Flow<Resource<OrganisationSettings>> = performNetworkOperation(
        remoteCall = {
            remote.get()
        },
        localUpdate = {

        },
        transform = {
            it.mapToDomain()
        }
    )
}
