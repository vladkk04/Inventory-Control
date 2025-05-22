package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.flattingRemoteToLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOrganisationUsersUseCase(
    private val remote: OrganisationUserRemoteDataSource,
    private val local: OrganisationUserLocalDataSource,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<OrganisationUser>>> =
        remote.getAll().flattingRemoteToLocal(
            onFailureBlock = { e ->
                local.getAll().map { entities ->
                    Resource.Error(data = entities.map { it.mapToDomain() }, errorMessage = e)
                }
            },
            onSuccessBlock = { response ->
                local.refresh(response.map { it.mapToEntity() })
                local.getAll().map { entities ->
                    Resource.Success(entities.map { it.mapToDomain() })
                }
            }
        )
}
