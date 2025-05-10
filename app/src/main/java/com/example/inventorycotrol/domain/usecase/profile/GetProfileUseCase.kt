package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.mapToDomain
import com.example.inventorycotrol.domain.model.user.Profile
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetProfileUseCase(
    private val userRemote: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getProfile(): Flow<Resource<Profile?>> {
        return userRemote.getUser().flatMapLatest { response ->
            when (response) {
                ApiResponseResult.Loading -> {
                    flowOf(Resource.Loading)
                }

                is ApiResponseResult.Failure -> {
                    flowOf(Resource.Error(errorMessage = response.errorMessage))
                }

                is ApiResponseResult.Success -> {
                    userLocalDataSource.getProfile(response.data.id)
                        .map { Resource.Success(it?.mapToDomain()) }
                }
            }
        }
    }


}