package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.OrganisationDto
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface OrganisationApiService {

    @GET("/organisations/{id}")
    suspend fun get(@Path("id") id: String): Response<OrganisationDto>

    @GET("/organisations")
    suspend fun getAll(): Response<List<OrganisationDto>>

    @POST("/organisations/create")
    suspend fun create(@Body request: OrganisationRequest): Response<OrganisationDto>

    @POST("/organisations/{id}")
    suspend fun leave(@Path("id") id: String)

    @PUT("/organisations/{id}")
    suspend fun update(@Path("id") id: String, @Body request: OrganisationRequest)

    @DELETE("/organisations/{id}")
    suspend fun delete(@Path("id") id: String)

}