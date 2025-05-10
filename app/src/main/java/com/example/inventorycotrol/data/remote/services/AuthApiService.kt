package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.domain.model.auth.AuthenticateResponse
import com.example.inventorycotrol.domain.model.auth.ForgotPasswordRequest
import com.example.inventorycotrol.domain.model.auth.ResetPasswordRequest
import com.example.inventorycotrol.domain.model.auth.SignInRequest
import com.example.inventorycotrol.domain.model.auth.SignInResponse
import com.example.inventorycotrol.domain.model.auth.SignUpRequest
import com.example.inventorycotrol.domain.model.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @POST("sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("sign-up")
    suspend fun signUp(@Body request: SignUpRequest)

    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest)

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)

    @GET("validate-otp")
    suspend fun validateOtp(@Query("email") email: String, @Query("otp") otp: String): Response<TokenResponse>

    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token: String): Response<AuthenticateResponse>
}