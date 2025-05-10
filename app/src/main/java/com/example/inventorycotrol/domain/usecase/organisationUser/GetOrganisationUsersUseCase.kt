package com.example.inventorycotrol.domain.usecase.organisationUser

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetOrganisationUsersUseCase(
    private val remote: OrganisationUserRemoteDataSource,
    private val local: OrganisationUserLocalDataSource,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<OrganisationUser>>> = remote.getAll()
        .flatMapLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> flowOf(Resource.Loading)
                is ApiResponseResult.Failure -> flowOf(Resource.Error(errorMessage = response.errorMessage))
                is ApiResponseResult.Success -> {
                    local.refresh(response.data.map { it.mapToEntity() })
                    local.getAll().map { entities ->
                        Resource.Success(entities.map { it.mapToDomain() })
                    }
                }
            }
        }
        .onStart { emit(Resource.Loading) }
        .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
}
