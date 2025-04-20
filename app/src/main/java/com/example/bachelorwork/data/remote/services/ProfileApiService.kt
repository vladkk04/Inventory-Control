package com.example.bachelorwork.data.remote.services

import com.example.bachelorwork.data.remote.dto.OrganisationInvitationDto
import retrofit2.Response
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

}