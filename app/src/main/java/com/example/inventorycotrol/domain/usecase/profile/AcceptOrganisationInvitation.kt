package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource

class AcceptOrganisationInvitation(
    private val repository: ProfileRemoteDataSource
) {
    suspend operator fun invoke(id: String) = repository.acceptOrganisationInvitation(id)
}
