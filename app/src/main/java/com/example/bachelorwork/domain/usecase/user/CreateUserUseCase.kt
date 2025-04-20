package com.example.bachelorwork.domain.usecase.user

import com.example.bachelorwork.data.local.entities.UserEntity
import com.example.bachelorwork.domain.repository.local.UserLocalDataSource

class CreateUserUseCase(
    private val userRepository: UserLocalDataSource
) {
    suspend operator fun invoke(user: UserEntity) {
        userRepository.insert(user)
    }
}