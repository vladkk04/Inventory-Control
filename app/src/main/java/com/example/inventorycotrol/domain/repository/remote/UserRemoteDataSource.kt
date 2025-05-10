package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    suspend fun userId(): String

    suspend fun getUser(): Flow<ApiResponseResult<UserDto>>

    suspend fun getUserById(id: String): Flow<ApiResponseResult<UserDto>>

    suspend fun getUserByEmail(email: String): Flow<ApiResponseResult<UserDto>>

}