package com.example.bachelorwork.domain.usecase.organisation

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.local.mappers.mapToDomain
import com.example.bachelorwork.data.remote.mappers.mapToEntity
import com.example.bachelorwork.domain.model.organisation.Organisation
import com.example.bachelorwork.domain.repository.local.OrganisationLocalDataSource
import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.performNetworkOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetOrganisationsUseCase(
    private val remote: OrganisationRemoteDataSource,
    private val local: OrganisationLocalDataSource,
) {

    suspend fun getOrganisationId() = remote.selectedOrganisationId()

    fun getOrganisation() =
        performNetworkOperation(
            remoteCall = { remote.get() },
            localUpdate = { local.update(it.mapToEntity()) },
            transform = { local.getOrganisationDetail(getOrganisationId() ?: "") }
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<Organisation>>>  {
        return remote.getAll()
            .flatMapLatest { response ->
                when (response) {
                    ApiResponseResult.Loading -> flowOf(Resource.Loading)
                    is ApiResponseResult.Failure -> {
                        flowOf(Resource.Error(errorMessage = response.errorMessage))
                    }
                    is ApiResponseResult.Success -> {
                        local.refresh(response.data.map { it.mapToEntity() })
                        local.getAll().map { entities ->
                            val list = entities.map { it.mapToDomain() }
                            Resource.Success(list)
                        }
                    }
                }
            }
            .onStart { emit(Resource.Loading) }
            .catch { e -> emit(Resource.Error(errorMessage = e.message ?: "Unknown error")) }
    }

}
