package com.example.bachelorwork.data.remote.services

import com.example.bachelorwork.domain.model.auth.AuthenticateResponse
import com.example.bachelorwork.domain.model.auth.ForgotPasswordRequest
import com.example.bachelorwork.domain.model.auth.ResetPasswordRequest
import com.example.bachelorwork.domain.model.auth.SignInRequest
import com.example.bachelorwork.domain.model.auth.SignInResponse
import com.example.bachelorwork.domain.model.auth.SignUpRequest
import com.example.bachelorwork.domain.model.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @POST("sign_in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("sign_up")
    suspend fun signUp(@Body request: SignUpRequest)

    @POST("forgot_password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest)

    @POST("reset_password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)

    @GET("validate_otp")
    suspend fun validateOtp(@Query("email") email: String, @Query("otp") otp: String): Response<TokenResponse>

    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token: String): Response<AuthenticateResponse>
}