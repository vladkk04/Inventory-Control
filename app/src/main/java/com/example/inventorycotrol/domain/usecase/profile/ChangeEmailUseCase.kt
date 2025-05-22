package com.example.inventorycotrol.domain.usecase.profile

import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.model.auth.requests.ChangeEmailRequest
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.performNetworkOperation

class ChangeEmailUseCase(
    private val remote: ProfileRemoteDataSource,
    private val local: UserLocalDataSource,
) {

    operator fun invoke(newEmail: String) = performNetworkOperation(
        remoteCall = { remote.changeEmail(ChangeEmailRequest(newEmail)) },
        localUpdate = { local.update(it.mapToEntity()) }
    )

}
