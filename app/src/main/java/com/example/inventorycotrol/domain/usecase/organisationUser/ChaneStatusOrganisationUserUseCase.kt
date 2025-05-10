package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource

class ChaneStatusOrganisationUserUseCase(
    private val remote: OrganisationUserRemoteDataSource,
) {

    suspend fun makeUserInactive(organisationUserId: String) =
        remote.makeUserInactive(organisationUserId)

    suspend fun makeUserActive(organisationUserId: String) =
        remote.makeUserInactive(organisationUserId)

}