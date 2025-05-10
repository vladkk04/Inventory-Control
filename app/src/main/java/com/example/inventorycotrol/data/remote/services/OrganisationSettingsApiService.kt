package com.example.inventorycotrol.data.remote.services

import OrganisationSettingsRequest
import com.example.inventorycotrol.data.remote.dto.OrganisationSettingsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface OrganisationSettingsApiService {

    @GET("/organisations/{id}/organisation-settings")
    suspend fun get(@Path("id") organisationId: String): Response<OrganisationSettingsDto>

    @PUT("/organisations/{id}/organisation-settings")
    suspend fun update(
        @Path("id") organisationId: String,
        @Body request: OrganisationSettingsRequest
    )
}