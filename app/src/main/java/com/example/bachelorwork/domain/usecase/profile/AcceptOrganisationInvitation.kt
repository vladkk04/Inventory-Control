package com.example.bachelorwork.domain.usecase.profile

import com.example.bachelorwork.domain.repository.remote.ProfileRemoteDataSource

class AcceptOrganisationInvitation(
    private val repository: ProfileRemoteDataSource
) {
    suspend operator fun invoke(id: String) = repository.acceptOrganisationInvitation(id)
}
