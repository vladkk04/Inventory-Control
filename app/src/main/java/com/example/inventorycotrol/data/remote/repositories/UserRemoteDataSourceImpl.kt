package com.example.inventorycotrol.data.remote.repositories

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.UserDto
import com.example.inventorycotrol.data.remote.services.UserApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRemoteDataSourceImpl(
    private val api: UserApiService,
    private val dataStoreManager: DataStoreManager,
) : UserRemoteDataSource {

    override suspend fun userId(): String =
        dataStoreManager.getPreference(AppConstants.USER_ID_KEY).firstOrNull() ?: ""

    override suspend fun getUser(): Flow<ApiResponseResult<UserDto>> =
        safeResponseApiCallFlow { api.getUserById(userId()) }

    override suspend fun getUserById(id: String): Flow<ApiResponseResult<UserDto>> {
        return safeResponseApiCallFlow { api.getUserById(id) }
    }

    override suspend fun getUserByEmail(email: String): Flow<ApiResponseResult<UserDto>> {
        return safeResponseApiCallFlow { api.getUserByEmail(email) }
    }
}