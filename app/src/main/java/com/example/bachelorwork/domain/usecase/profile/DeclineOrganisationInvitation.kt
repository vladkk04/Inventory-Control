package com.example.bachelorwork.domain.usecase.profile

import com.example.bachelorwork.domain.repository.remote.ProfileRemoteDataSource

class DeclineOrganisationInvitation(
    private val repository: ProfileRemoteDataSource
) {
    suspend operator fun invoke(id: String) = repository.declineOrganisationInvitation(id)
}
