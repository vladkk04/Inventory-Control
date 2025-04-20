package com.example.bachelorwork.data.remote.repositories

import com.auth0.android.jwt.JWT
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants.JWT_ACCESS_TOKEN_KEY
import com.example.bachelorwork.data.constants.AppConstants.JWT_REFRESH_TOKEN_KEY
import com.example.bachelorwork.data.constants.AppConstants.RESET_PASSWORD_TOKEN_KEY
import com.example.bachelorwork.data.constants.AppConstants.SELECTED_ORGANISATION_ID
import com.example.bachelorwork.data.constants.AppConstants.USER_ID_KEY
import com.example.bachelorwork.data.remote.services.AuthApiService
import com.example.bachelorwork.data.remote.services.RefreshApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.auth.AuthenticateResponse
import com.example.bachelorwork.domain.model.auth.ForgotPasswordRequest
import com.example.bachelorwork.domain.model.auth.OtpRequest
import com.example.bachelorwork.domain.model.auth.ResetPasswordRequest
import com.example.bachelorwork.domain.model.auth.SignInRequest
import com.example.bachelorwork.domain.model.auth.SignInResponse
import com.example.bachelorwork.domain.model.auth.SignUpRequest
import com.example.bachelorwork.domain.model.auth.TokenResponse
import com.example.bachelorwork.domain.repository.remote.AuthRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApiService,
    private val refreshApiService: RefreshApiService,
    private val dataStoreManager: DataStoreManager,
) : AuthRemoteDataSource {

    override suspend fun getUserId(): Flow<String?> = dataStoreManager.getPreference(USER_ID_KEY)

    /*override suspend fun refreshToken(token: String): Response<TokenResponse> {
        val response = refreshApiService.refreshToken(token)
        val preferencePair = JWT_ACCESS_TOKEN_KEY to token

        response.body()?.token?.let { dataStoreManager.savePreference(preferencePair) }

        return response
    }

    override suspend fun signIn(email: String, password: String): Response<SignInResponse> {
        val response = api.signIn(SignInRequest(email, password))

        response.body()?.let {
            dataStoreManager.savePreference(JWT_ACCESS_TOKEN_KEY to it.accessToken)
            dataStoreManager.savePreference(JWT_REFRESH_TOKEN_KEY to it.refreshToken)
        }

        return response
    }

    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): ApiResponseResult<Unit> =
        safeApiCall { api.signUp(SignUpRequest(fullName, email, password)) }

    override suspend fun signOut(): Boolean {
        dataStoreManager.deletePreference(JWT_ACCESS_TOKEN_KEY)
        dataStoreManager.deletePreference(JWT_REFRESH_TOKEN_KEY)
        return true
    }

    override suspend fun forgotPassword(email: String): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow { api.forgotPassword(ForgotPasswordRequest(email)) }
    }

    override suspend fun resetPassword(
        email: String,
        password: String,
    ): Flow<ApiResponseResult<Unit>> {
        return safeApiCallFlow {
            dataStoreManager.getPreference(RESET_PASSWORD_TOKEN_KEY)
                .firstOrNull()
                ?.let {
                    api.resetPassword(ResetPasswordRequest(email, password, it))
                    dataStoreManager.deletePreference(RESET_PASSWORD_TOKEN_KEY)
                }
        }
    }

    override suspend fun validateOtp(email: String, otp: String): Response<TokenResponse> {
        val response = api.validateOtp(email, otp)
        response.body()?.token?.let {
            dataStoreManager.savePreference(RESET_PASSWORD_TOKEN_KEY to it)
        }

        return response
    }


    override suspend fun authenticate(): Response<AuthenticateResponse> {
        val token = dataStoreManager.getPreference(JWT_ACCESS_TOKEN_KEY).firstOrNull()
        return api.authenticate("Bearer $token")
    }*/
    override suspend fun signIn(request: SignInRequest): Flow<ApiResponseResult<SignInResponse>> =
        safeResponseApiCallFlow {
            api.signIn(request).also {
                it.body()?.let { signInResponse ->
                    val jwt = JWT(signInResponse.accessToken)

                    jwt.getClaim("userId").asString()?.let { userId ->
                        dataStoreManager.savePreference(USER_ID_KEY to userId)
                    }

                    dataStoreManager.savePreference(JWT_ACCESS_TOKEN_KEY to signInResponse.accessToken)
                    dataStoreManager.savePreference(JWT_REFRESH_TOKEN_KEY to signInResponse.refreshToken)
                }
            }
        }

    override suspend fun signUp(request: SignUpRequest): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.signUp(request) }

    override suspend fun refreshToken(token: String): Flow<ApiResponseResult<TokenResponse>> =
        safeResponseApiCallFlow {
            refreshApiService.refreshToken(token).also {
                it.body()?.token?.let { token ->
                    dataStoreManager.savePreference(JWT_ACCESS_TOKEN_KEY to token)
                }
            }
        }


    override suspend fun forgotPassword(request: ForgotPasswordRequest): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.forgotPassword(request) }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow {
            dataStoreManager.getPreference(RESET_PASSWORD_TOKEN_KEY)
                .first()?.let {
                    api.resetPassword(ResetPasswordRequest(email, password, it))
                    dataStoreManager.deletePreference(RESET_PASSWORD_TOKEN_KEY)
                }
        }


    override suspend fun validateOtp(request: OtpRequest): Flow<ApiResponseResult<TokenResponse>> =
        safeResponseApiCallFlow {
            api.validateOtp(request.email, request.otp).also {
                it.body()?.token?.let { token ->
                    dataStoreManager.savePreference(RESET_PASSWORD_TOKEN_KEY to token)
                }
            }
        }

    override suspend fun authenticate(): Flow<ApiResponseResult<AuthenticateResponse>> =
        safeResponseApiCallFlow {
            val token = dataStoreManager.getPreference(JWT_ACCESS_TOKEN_KEY).firstOrNull()
            api.authenticate("Bearer $token")
        }

    override suspend fun signOut(): Flow<ApiResponseResult<Unit>> = safeApiCallFlow {
        dataStoreManager.deletePreference(USER_ID_KEY)
        dataStoreManager.deletePreference(SELECTED_ORGANISATION_ID)
        dataStoreManager.deletePreference(JWT_ACCESS_TOKEN_KEY)
        dataStoreManager.deletePreference(JWT_REFRESH_TOKEN_KEY)
    }
}