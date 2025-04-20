package com.example.bachelorwork.data.remote.interceptors

import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val authTokenManager: DataStoreManager,
    private val tokenType: TokenInterceptorType
) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val preferenceToken = when (tokenType) {
            TokenInterceptorType.ACCESS -> AppConstants.JWT_ACCESS_TOKEN_KEY
            TokenInterceptorType.REFRESH -> AppConstants.JWT_REFRESH_TOKEN_KEY
        }

        val token = runBlocking {
            authTokenManager.getPreference(preferenceToken).first()
        }

        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
            .build()

        return chain.proceed(newRequest)
    }
}