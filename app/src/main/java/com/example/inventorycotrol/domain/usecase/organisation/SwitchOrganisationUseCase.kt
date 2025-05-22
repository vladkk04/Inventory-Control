package com.example.inventorycotrol.domain.usecase.organisation

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.common.Resource
import com.example.inventorycotrol.data.remote.mappers.mapToEntity
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.onStart

class SwitchOrganisationUseCase(
    private val userRemote: UserRemoteDataSource,
    private val userLocal: UserLocalDataSource,
    private val organisationUserRemote: OrganisationUserRemoteDataSource,
    private val organisationUserLocal: OrganisationUserLocalDataSource,
    private val organisationRemote: OrganisationRemoteDataSource,
    private val dataStoreManager: DataStoreManager,
) {
    operator fun invoke(organisationId: String) = flow {
        val organisationUser = organisationUserRemote.getByUserId().lastOrNull()
            ?: throw Exception("Organisation user not found")

        when (organisationUser) {
            ApiResponseResult.Loading -> {

            }

            is ApiResponseResult.Failure -> {
                emit(Resource.Error<Unit>(errorMessage = organisationUser.errorMessage))
            }

            is ApiResponseResult.Success -> {
                organisationUserLocal.upsert(organisationUser.data.mapToEntity())
            }
        }

        val user = userRemote.getUser().lastOrNull() ?:
            throw Exception("User not found")

        when (user) {
            ApiResponseResult.Loading -> {

            }

            is ApiResponseResult.Failure -> {
                emit(Resource.Error<Unit>(errorMessage = user.errorMessage))
            }

            is ApiResponseResult.Success -> {
                userLocal.upsert(user.data.mapToEntity())
                organisationRemote.switchOrganisation(organisationId)
                emit(Resource.Success(Unit))
            }
        }

    }.catch { e -> emit(Resource.Error(errorMessage = e.message.toString())) }
        .onStart { emit(Resource.Loading) }
}
