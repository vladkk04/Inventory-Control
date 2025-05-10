package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.OrganisationInvitationDto
import com.example.inventorycotrol.data.remote.dto.UserDto
import com.example.inventorycotrol.domain.model.auth.ChangeEmailRequest
import com.example.inventorycotrol.domain.model.auth.ChangePasswordRequest
import com.example.inventorycotrol.domain.model.profile.ChangeInfoUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApiService {

    @GET("profile/organisations-invitations")
    suspend fun getOrganisationsInviting(): Response<List<OrganisationInvitationDto>>

    @POST("profile/organisations-invitations/{invitationId}/accept")
    suspend fun acceptOrganisationInvitation(@Path("invitationId") id: String)

    @POST("profile/organisations-invitations/{invitationId}/decline")
    suspend fun declineOrganisationInvitation(@Path("invitationId") id: String)

    @POST("profile/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest)

    @POST("profile/change-email")
    suspend fun changeEmail(@Body request: ChangeEmailRequest): Response<UserDto>

    @POST("profile/change-info")
    suspend fun changeInfoUser(@Body request: ChangeInfoUserRequest): Response<UserDto>


}