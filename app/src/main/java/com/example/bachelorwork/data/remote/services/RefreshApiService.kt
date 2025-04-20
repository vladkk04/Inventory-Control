package com.example.bachelorwork.data.remote.services

import com.example.bachelorwork.domain.model.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshApiService {

    @POST("refresh_token")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<TokenResponse>
}