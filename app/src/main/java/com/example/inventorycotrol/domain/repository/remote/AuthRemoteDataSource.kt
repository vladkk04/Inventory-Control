package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.auth.responses.AuthenticateResponse
import com.example.inventorycotrol.domain.model.auth.requests.ForgotPasswordRequest
import com.example.inventorycotrol.domain.model.auth.requests.OtpRequest
import com.example.inventorycotrol.domain.model.auth.requests.SignInRequest
import com.example.inventorycotrol.domain.model.auth.responses.SignInResponse
import com.example.inventorycotrol.domain.model.auth.requests.SignUpRequest
import com.example.inventorycotrol.domain.model.auth.responses.TokenResponse
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