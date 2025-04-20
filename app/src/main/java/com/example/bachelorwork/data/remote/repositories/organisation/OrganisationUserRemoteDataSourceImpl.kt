package com.example.bachelorwork.data.remote.repositories.organisation

import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.constants.AppConstants.SELECTED_ORGANISATION_ID
import com.example.bachelorwork.data.remote.dto.OrganisationUserDto
import com.example.bachelorwork.data.remote.services.OrganisationUserApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.bachelorwork.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserUpdateRequest
import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OrganisationUserRemoteDataSourceImpl(
    private val api: OrganisationUserApiService,
    private val dataStoreManager: DataStoreManager,
) : OrganisationUserRemoteDataSource {

    private suspend fun organisationId() =
        dataStoreManager.getPreference(SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw Exception("Organisation id not found")

    private suspend fun userId() =
        dataStoreManager.getPreference(AppConstants.USER_ID_KEY).firstOrNull()
            ?: throw Exception("User id not found")

    override suspend fun update(
        organisationUserId: String,
        request: OrganisationUserUpdateRequest
    ): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.update(organisationId(), organisationUserId, request) }
    }

    override suspend fun assignRole(
        organisationUserId: String,
        request: OrganisationUserAssignRoleRequest
    ): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.assignRole(organisationId(), organisationUserId, request) }
    }

    override suspend fun getAll(): Flow<ApiResponseResult<List<OrganisationUserDto>>> {
        return safeResponseApiCallFlow { api.getAll(organisationId()) }
    }

    override suspend fun getByUserId(): Flow<ApiResponseResult<OrganisationUserDto>> {
        return safeResponseApiCallFlow { api.getByUserId(organisationId(), userId()) }
    }

    override suspend fun delete(organisationUserId: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.delete(organisationId(), organisationUserId) }
    }

    override suspend fun makeUserActive(organisationUserId: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.makeUserActive(organisationId(), organisationUserId) }
    }

    override suspend fun makeUserInactive(organisationUserId: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.makeUserInactive(organisationId(), organisationUserId) }
    }

    override suspend fun cancelInviteByUserId(userId: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.cancelInviteByUserId(organisationId(), userId) }
    }

    override suspend fun cancelInviteByUserEmail(email: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.cancelInviteByUserEmail(organisationId(), email) }
    }

    override suspend fun inviteUserByUserId(request: OrganisationInvitationUserIdRequest): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow {
            api.inviteUserByUserId(organisationId(), request)
        }
    }

    override suspend fun inviteUserByEmail(request: OrganisationInvitationEmailRequest): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow {
            api.inviteUserByEmail(organisationId(), request)
        }
    }

}