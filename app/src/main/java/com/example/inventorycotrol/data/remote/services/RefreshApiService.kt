package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.domain.model.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshApiService {

    @POST("refresh_token")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<TokenResponse>
}