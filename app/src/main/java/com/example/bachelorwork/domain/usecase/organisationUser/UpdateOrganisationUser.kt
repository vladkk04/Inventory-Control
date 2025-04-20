package com.example.bachelorwork.domain.usecase.organisationUser

import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserUpdateRequest
import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource

class UpdateOrganisationUser(
    private val remote: OrganisationUserRemoteDataSource
) {
    suspend operator fun invoke(
        organisationUserId: String,
        request: OrganisationUserUpdateRequest
    ) = remote.update(organisationUserId, request)
}