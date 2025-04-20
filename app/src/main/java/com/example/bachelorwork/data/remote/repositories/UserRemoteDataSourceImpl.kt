package com.example.bachelorwork.data.remote.repositories

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.UserDto
import com.example.bachelorwork.data.remote.services.UserApiService
import com.example.bachelorwork.domain.repository.remote.UserRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow

class UserRemoteDataSourceImpl(
    private val api: UserApiService,
): UserRemoteDataSource {

    override suspend fun getUserById(id: String): Flow<ApiResponseResult<UserDto>> {
        return safeResponseApiCallFlow { api.getUserById(id) }
    }

    override suspend fun getUserByEmail(email: String): Flow<ApiResponseResult<UserDto>> {
        return safeResponseApiCallFlow { api.getUserByEmail(email) }
    }
}