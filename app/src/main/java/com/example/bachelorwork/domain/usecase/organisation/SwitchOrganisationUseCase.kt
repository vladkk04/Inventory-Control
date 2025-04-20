package com.example.bachelorwork.domain.usecase.organisation

import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class SwitchOrganisationUseCase(
    private val remoteDataSource: OrganisationRemoteDataSource,
) {
    operator fun invoke(organisationId: String) = flow<Resource<Unit>> {
        remoteDataSource.switchOrganisation(organisationId)
        emit(Resource.Success(Unit))
    }.catch { e -> emit(Resource.Error(errorMessage = e.message.toString())) }
        .onStart { emit(Resource.Loading) }
}
