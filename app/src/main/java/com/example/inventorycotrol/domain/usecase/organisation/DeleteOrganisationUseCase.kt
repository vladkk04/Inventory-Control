package com.example.inventorycotrol.domain.usecase.organisation

import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource

class DeleteOrganisationUseCase(
    private val remote: OrganisationRemoteDataSource,
    private val local: OrganisationLocalDataSource
) {
    suspend operator fun invoke() = remote.delete()
}
