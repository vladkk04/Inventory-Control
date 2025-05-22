package com.example.inventorycotrol.data.remote.repositories

import com.auth0.android.jwt.JWT
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants.JWT_ACCESS_TOKEN_KEY
import com.example.inventorycotrol.data.constants.AppConstants.JWT_REFRESH_TOKEN_KEY
import com.example.inventorycotrol.data.constants.AppConstants.RESET_PASSWORD_TOKEN_KEY
import com.example.inventorycotrol.data.constants.AppConstants.SELECTED_ORGANISATION_ID
import com.example.inventorycotrol.data.constants.AppConstants.USER_ID_KEY
import com.example.inventorycotrol.data.remote.services.AuthApiService
import com.example.inventorycotrol.data.remote.services.RefreshApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.auth.responses.AuthenticateResponse
import com.example.inventorycotrol.domain.model.auth.requests.ForgotPasswordRequest
import com.example.inventorycotrol.domain.model.auth.requests.OtpRequest
import com.example.inventorycotrol.domain.model.auth.requests.ResetPasswordRequest
import com.example.inventorycotrol.domain.model.auth.requests.SignInRequest
import com.example.inventorycotrol.domain.model.auth.responses.SignInResponse
import com.example.inventorycotrol.domain.model.auth.requests.SignUpRequest
import com.example.inventorycotrol.domain.model.auth.responses.TokenResponse
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeSuspendResponseApiCallFlow
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

    override suspend fun signIn(request: SignInRequest): Flow<ApiResponseResult<SignInResponse>> =
        safeSuspendResponseApiCallFlow {
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
        safeSuspendResponseApiCallFlow {
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
        safeSuspendResponseApiCallFlow {
            api.validateOtp(request.email, request.otp).also {
                it.body()?.token?.let { token ->
                    dataStoreManager.savePreference(RESET_PASSWORD_TOKEN_KEY to token)
                }
            }
        }

    override suspend fun authenticate(): Flow<ApiResponseResult<AuthenticateResponse>> =
        safeSuspendResponseApiCallFlow {
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