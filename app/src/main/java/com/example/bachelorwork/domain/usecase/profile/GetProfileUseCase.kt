package com.example.bachelorwork.domain.usecase.profile

import com.example.bachelorwork.common.Resource
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.local.entities.mapToDomain
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.user.Profile
import com.example.bachelorwork.domain.repository.local.UserLocalDataSource
import com.example.bachelorwork.domain.repository.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetProfileUseCase(
    private val userRemote: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val dataStoreManager: DataStoreManager
) {
    fun getProfile(): Flow<Resource<Profile>> =  flow {
        try {
            val userId = dataStoreManager.getPreference(AppConstants.USER_ID_KEY).firstOrNull()
            val userProfile = userId?.let { userLocalDataSource.getProfile(it).firstOrNull() }

            if (userProfile == null) {
                emit(Resource.Error(errorMessage = "User profile not found"))
                return@flow
            }

            emit(Resource.Success(userProfile.mapToDomain()))
        } catch (e: Throwable) {
            emit(Resource.Error(errorMessage = e.message ?: "Unknown error"))
        }
    }





}