package com.example.inventorycotrol.domain.usecase.organisation

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest
import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull

class CreateOrganisationUseCase(
    private val organisationUserLocal: OrganisationUserLocalDataSource,
    private val organisationUserRemoteDataSource: OrganisationUserRemoteDataSource,
    private val remote: OrganisationRemoteDataSource,
    private val local: OrganisationLocalDataSource
) {
    operator fun invoke(request: OrganisationRequest): Flow<Resource<Unit>> = flow {
        remote.create(request).lastOrNull()?.let { organisationResponse ->
            when (organisationResponse) {
                ApiResponseResult.Loading -> {
                    emit(Resource.Loading)
                }

                is ApiResponseResult.Failure -> {
                    emit(Resource.Error<Unit>(errorMessage = organisationResponse.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    local.insert(organisationResponse.data.mapToEntity())

                    when (val response = organisationUserRemoteDataSource.getByUserId().last()) {
                        ApiResponseResult.Loading -> {

                        }
                        is ApiResponseResult.Failure -> {
                            emit(Resource.Error<Unit>(errorMessage = response.errorMessage))
                        }
                        is ApiResponseResult.Success -> {
                            val organisationUserEntity = response.data.mapToEntity()

                            organisationUserLocal.upsert(organisationUserEntity)
                        }
                    }

                    emit(Resource.Success(Unit))
                }
            }
        }
    }.catch { emit(Resource.Error(errorMessage = it.message ?: "Unknown error")) }
}
