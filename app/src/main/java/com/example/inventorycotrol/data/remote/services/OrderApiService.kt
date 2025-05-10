package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.domain.model.order.OrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApiService {

    @POST("/organisations/{id}/orders/create")
    suspend fun create(@Path("id") organisationId: String, @Body request: OrderRequest): Response<OrderDto>

    @PUT("/organisations/{id}/orders/{orderId}")
    suspend fun update(@Path("id") organisationId: String, @Path("orderId") orderId: String, @Body request: OrderRequest)

    @DELETE("/organisations/{id}/orders/{orderId}")
    suspend fun delete(@Path("id") organisationId: String, @Path("orderId") orderId: String)

    @GET("/organisations/{id}/orders/{orderId}")
    suspend fun get(@Path("id") organisationId: String, @Path("orderId") orderId: String): Response<OrderDto>

    @GET("/organisations/{id}/orders")
    suspend fun getAll(@Path("id") organisationId: String): Response<List<OrderDto>>

}