package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.entities.mapToDomain
import com.example.inventorycotrol.domain.model.user.Profile
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.flattingRemoteToLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProfileUseCase(
    private val userRemote: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getProfile(): Flow<Resource<Profile?>> {
        return userRemote.getUser().flattingRemoteToLocal(
            onFailureBlock = { errorMessage ->
                val userId = userRemote.userId()
                val user = userLocalDataSource.getProfile(userId).map { it?.mapToDomain() }

                user.map { Resource.Error(data = it, errorMessage = errorMessage) }
            },
            onSuccessBlock = { response ->
                userLocalDataSource.getProfile(response.id).map { Resource.Success(it?.mapToDomain()) }
            }
        )
    }


}