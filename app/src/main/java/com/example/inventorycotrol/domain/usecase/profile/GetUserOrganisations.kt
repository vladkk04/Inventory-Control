package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource

class GetUserOrganisations(
    private val organisationRepository: OrganisationRemoteDataSource
) {

}
