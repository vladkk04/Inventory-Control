package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource

class DeclineOrganisationInvitation(
    private val repository: ProfileRemoteDataSource
) {
    suspend operator fun invoke(id: String) = repository.declineOrganisationInvitation(id)
}
