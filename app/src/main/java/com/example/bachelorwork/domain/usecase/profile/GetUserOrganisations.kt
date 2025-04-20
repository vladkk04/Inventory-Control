package com.example.bachelorwork.domain.usecase.profile

import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource

class GetUserOrganisations(
    private val organisationRepository: OrganisationRemoteDataSource
) {

}
