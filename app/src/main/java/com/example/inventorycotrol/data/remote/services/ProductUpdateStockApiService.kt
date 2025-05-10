package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.ChangeStockProductDto
import com.example.inventorycotrol.data.remote.dto.ProductUpdateStockDto
import com.example.inventorycotrol.data.remote.dto.ProductUpdateStockViewDto
import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductUpdateStockApiService {

    @POST("/organisations/{id}/products/update-stock")
    suspend fun create(
        @Path("id") organisationId: String,
        @Body request: ProductUpdateStockRequest
    ): Response<ProductUpdateStockDto>

    @GET("/organisations/{id}/products/{productId}/update-stock")
    suspend fun getAllByProductId(
        @Path("id") organisationId: String,
        @Path("productId") productId: String,
    ): Response<List<ChangeStockProductDto>>

    @GET("/organisations/{id}/products/update-stock")
    suspend fun getAllByOrganisation(
        @Path("id") organisationId: String,
    ): Response<List<ProductUpdateStockDto>>

    @GET("/organisations/{id}/products/update-stock-view")
    suspend fun getAllByOrganisationView(
        @Path("id") organisationId: String,
    ): Response<List<ProductUpdateStockViewDto>>

}