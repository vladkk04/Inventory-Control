package com.example.bachelorwork.domain.usecase.organisation

import com.example.bachelorwork.domain.repository.local.OrganisationLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource

class DeleteOrganisationUseCase(
    private val remote: OrganisationRemoteDataSource,
    private val local: OrganisationLocalDataSource
) {
    suspend operator fun invoke() = remote.delete()
}
