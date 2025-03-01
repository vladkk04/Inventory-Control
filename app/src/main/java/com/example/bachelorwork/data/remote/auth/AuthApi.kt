package com.example.bachelorwork.data.remote.auth

import com.example.bachelorwork.domain.model.auth.AuthRequest
import com.example.bachelorwork.domain.model.auth.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("signin")
    suspend fun signIn(@Body request: AuthRequest): TokenResponse

    @POST("signup")
    suspend fun signUp(@Body request: AuthRequest): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}