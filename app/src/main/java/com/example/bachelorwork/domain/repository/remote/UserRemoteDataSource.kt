package com.example.bachelorwork.domain.repository.remote

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    suspend fun getUserById(id: String): Flow<ApiResponseResult<UserDto>>

    suspend fun getUserByEmail(email: String): Flow<ApiResponseResult<UserDto>>

}