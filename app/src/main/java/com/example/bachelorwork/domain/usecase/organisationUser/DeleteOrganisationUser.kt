package com.example.bachelorwork.domain.usecase.organisationUser

import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource

class DeleteOrganisationUser(
    private val organisationUserRemoteDataSource: OrganisationUserRemoteDataSource
) {
    suspend fun delete(organisationUserId: String) = organisationUserRemoteDataSource.delete(organisationUserId)
}