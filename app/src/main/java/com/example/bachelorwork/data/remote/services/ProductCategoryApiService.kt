package com.example.bachelorwork.data.remote.services

import com.example.bachelorwork.data.remote.dto.ProductCategoryDto
import com.example.bachelorwork.domain.model.category.ProductCategoryRequest
import com.example.bachelorwork.domain.model.category.CreateProductCategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductCategoryApiService {

    @POST("/organisations/{id}/categories/create")
    suspend fun create(@Path("id") organisationId: String, @Body request: ProductCategoryRequest): Response<CreateProductCategoryResponse>

    @PUT("/organisations/{id}/categories/{categoryName}")
    suspend fun update(@Path("id") organisationId: String, @Path("categoryName") categoryId: String, @Body request: ProductCategoryRequest)

    @DELETE("/organisations/{id}/categories/{categoryName}")
    suspend fun delete(@Path("id") organisationId: String, @Path("categoryName") categoryId: String)

    @GET("/organisations/{id}/categories/{categoryName}")
    suspend fun get(@Path("id") organisationId: String, @Path("categoryName") categoryId: String): Response<ProductCategoryDto>

    @GET("/organisations/{id}/categories")
    suspend fun getAll(@Path("id") organisationId: String): Response<List<ProductCategoryDto>>
}
