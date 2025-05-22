package com.example.inventorycotrol.domain.usecase.user

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.local.mappers.mapToDomain
import com.example.inventorycotrol.data.remote.dto.UserDto
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.user.User
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf


class GetUserUseCase(
    private val remote: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun get(): Flow<Resource<User>> = remote.getUser().flatMapLatest { userDto ->
        when (userDto) {
            ApiResponseResult.Loading -> {
                flowOf(Resource.Loading)
            }
            is ApiResponseResult.Failure -> {
                flowOf(Resource.Error(errorMessage = userDto.errorMessage))
            }
            is ApiResponseResult.Success -> {
                localDataSource.upsert(userDto.data.mapToEntity())
                flowOf(Resource.Success(userDto.data.mapToEntity().mapToDomain()))
            }
        }
    }.catch { emit(Resource.Error(errorMessage = it.message.toString())) }

    suspend fun getById(id: String): Flow<ApiResponseResult<UserDto>> = remote.getUserById(id)

    suspend fun getByEmail(email: String): Flow<ApiResponseResult<UserDto>> = remote.getUserByEmail(email)
}