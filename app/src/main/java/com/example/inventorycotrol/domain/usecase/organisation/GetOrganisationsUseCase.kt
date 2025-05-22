package com.example.inventorycotrol.domain.usecase.organisation

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.organisation.Organisation
import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.flattingRemoteToLocal
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

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getOrganisation(): Flow<Resource<Organisation>> {
        return remote.get().flattingRemoteToLocal(
            onFailureBlock = { e->
                val id = remote.selectedOrganisationId() ?: ""
                local.getById(id).map { Resource.Error(data = it.mapToDomain(), errorMessage = e) }
            },
            onSuccessBlock = { response ->
                local.upsert(response.mapToEntity())
                local.getById(response.id).map { Resource.Success(it.mapToDomain()) }
            }
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<List<Organisation>>> {
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
