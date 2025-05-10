package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.OrganisationUserDto
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationEmailRequest
import com.example.inventorycotrol.domain.model.organisation.invitations.OrganisationInvitationUserIdRequest
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserAssignRoleRequest
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface OrganisationUserApiService {

    @GET("/organisations/{id}/users")
    suspend fun getAll(@Path("id") organisationId: String): Response<List<OrganisationUserDto>>

    @GET("/organisations/{id}/users/{userId}")
    suspend fun getByUserId(@Path("id") organisationId: String, @Path("userId") userId: String): Response<OrganisationUserDto>

    @PUT("/organisations/{id}/users/{organisationUserId}")
    suspend fun update(
        @Path("id") organisationId: String,
        @Path("organisationUserId") organisationUserId: String,
        @Body request: OrganisationUserUpdateRequest
    )

    @POST("/organisations/{id}/users/{organisationUserId}/assign-role")
    suspend fun assignRole(
        @Path("id") organisationId: String,
        @Path("organisationUserId") organisationUserId: String,
        @Body request: OrganisationUserAssignRoleRequest
    )

    @DELETE("/organisations/{id}/users/{organisationUserId}")
    suspend fun delete(
        @Path("id") organisationId: String,
        @Path("organisationUserId") organisationUserId: String
    )

    @POST("/organisations/{id}/users/{organisationUserId}/make-inactive")
    suspend fun makeUserInactive(
        @Path("id") organisationId: String,
        @Path("organisationUserId") organisationUserId: String
    )

    @POST("/organisations/{id}/users/{organisationUserId}/make-active")
    suspend fun makeUserActive(
        @Path("id") organisationId: String,
        @Path("organisationUserId") organisationUserId: String
    )

    @POST("/organisations/{id}/users/cancel-invite")
    suspend fun cancelInviteByUserId(
        @Path("id") organisationId: String,
        @Query("userId") userId: String
    )

    @POST("/organisations/{id}/users/cancel-invite")
    suspend fun cancelInviteByUserEmail(
        @Path("id") organisationId: String,
        @Query("email") email: String
    )

    @POST("/organisations/{id}/users/invite/userId")
    suspend fun inviteUserByUserId(
        @Path("id") organisationId: String,
        @Body request: OrganisationInvitationUserIdRequest
    ) : Response<OrganisationUserDto>

    @POST("/organisations/{id}/users/invite/email")
    suspend fun inviteUserByEmail(
        @Path("id") organisationId: String,
        @Body request: OrganisationInvitationEmailRequest
    ) : Response<OrganisationUserDto>

}