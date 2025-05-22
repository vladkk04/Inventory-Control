package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.domain.model.profile.OrganisationInvitation
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeSuspendResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetOrganisationsInviting(
    private val profileDataSource: ProfileRemoteDataSource,
) {
    operator fun invoke(): Flow<ApiResponseResult<List<OrganisationInvitation>>> = flow {
        safeSuspendResponseApiCallFlow { profileDataSource.getOrganisationsInviting() }.collect { response ->
            when (response) {
                is ApiResponseResult.Loading -> emit(ApiResponseResult.Loading)
                is ApiResponseResult.Failure -> emit(response)
                is ApiResponseResult.Success -> {
                    emit(ApiResponseResult.Success(response.data.map { it.mapToDomain() }))
                }
            }
        }
    }.catch { e ->
        emit(
            ApiResponseResult.Failure(
                errorMessage = e.message ?: "Unknown error",
                code = 404
            )
        )
    }
}
