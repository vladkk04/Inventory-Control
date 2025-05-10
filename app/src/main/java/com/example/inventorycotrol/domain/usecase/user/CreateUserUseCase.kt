package com.example.inventorycotrol.domain.usecase.user

import com.example.inventorycotrol.data.local.entities.UserEntity
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource

class CreateUserUseCase(
    private val userRepository: UserLocalDataSource
) {
    suspend operator fun invoke(user: UserEntity) {
        userRepository.insert(user)
    }
}