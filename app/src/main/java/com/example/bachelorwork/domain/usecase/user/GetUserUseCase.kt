package com.example.bachelorwork.domain.usecase.user

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.dto.UserDto
import com.example.bachelorwork.domain.repository.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow


class GetUserUseCase(
    private val userRepository: UserRemoteDataSource
) {
    suspend fun getById(id: String): Flow<ApiResponseResult<UserDto>> = userRepository.getUserById(id)

    suspend fun getByEmail(email: String): Flow<ApiResponseResult<UserDto>> = userRepository.getUserByEmail(email)
}