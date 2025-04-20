package com.example.bachelorwork.domain.usecase.organisationUser

import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource

class AssignRoleOrganisationUser(
    private val remote: OrganisationUserRemoteDataSource
) {
    suspend operator fun invoke(
        organisationUserId: String,
        request: OrganisationUserAssignRoleRequest
    ) = remote.assignRole(organisationUserId, request)
}