package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.ProductDto
import com.example.inventorycotrol.domain.model.product.ProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {

    @POST("/organisations/{id}/products/create")
    suspend fun create(@Path("id") organisationId: String, @Body request: ProductRequest): Response<ProductDto>

    @PUT("/organisations/{id}/products/{productId}")
    suspend fun update(@Path("id") organisationId: String, @Path("productId") productId: String, @Body request: ProductRequest)

    @DELETE("/organisations/{id}/products/{productId}")
    suspend fun delete(@Path("id") organisationId: String, @Path("productId") productId: String)

    @GET("/organisations/{id}/products/{productId}")
    suspend fun get(@Path("id") organisationId: String, @Path("productId") productId: String): Response<ProductDto>

    @GET("/organisations/{id}/products")
    suspend fun getAll(@Path("id") organisationId: String): Response<List<ProductDto>>


}