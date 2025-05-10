package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.profile.ChangeInfoUserRequest
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class ChangeUserInfoUseCase(
    private val remote: ProfileRemoteDataSource,
    private val local: UserLocalDataSource
) {

    operator fun invoke(request: ChangeInfoUserRequest) = performNetworkOperation(
        remoteCall = { remote.changeInfoUser(request) },
        localUpdate = { userDto ->
            local.update(userDto.mapToEntity())
        }
    )

}
