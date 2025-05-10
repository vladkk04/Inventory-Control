package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.domain.model.auth.ChangePasswordRequest
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource

class ChangePasswordUseCase(
    private val remote: ProfileRemoteDataSource
) {
    suspend operator fun invoke(oldPassword: String, newPassword: String) = remote.changePassword(
        ChangePasswordRequest(oldPassword, newPassword)
    )
}
