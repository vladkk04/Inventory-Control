package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.auth.AuthenticateResponse
import com.example.inventorycotrol.domain.model.auth.ForgotPasswordRequest
import com.example.inventorycotrol.domain.model.auth.OtpRequest
import com.example.inventorycotrol.domain.model.auth.SignInRequest
import com.example.inventorycotrol.domain.model.auth.SignInResponse
import com.example.inventorycotrol.domain.model.auth.SignUpRequest
import com.example.inventorycotrol.domain.model.auth.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {

    suspend fun getUserId(): Flow<String?>

    suspend fun signIn(request: SignInRequest): Flow<ApiResponseResult<SignInResponse>>

    suspend fun signUp(request: SignUpRequest): Flow<ApiResponseResult<Unit>>

    suspend fun refreshToken(token: String): Flow<ApiResponseResult<TokenResponse>>

    suspend fun forgotPassword(request: ForgotPasswordRequest): Flow<ApiResponseResult<Unit>>

    suspend fun resetPassword(email: String, password: String): Flow<ApiResponseResult<Unit>>

    suspend fun validateOtp(request: OtpRequest): Flow<ApiResponseResult<TokenResponse>>

    suspend fun authenticate(): Flow<ApiResponseResult<AuthenticateResponse>>

    suspend fun signOut(): Flow<ApiResponseResult<Unit>>
}