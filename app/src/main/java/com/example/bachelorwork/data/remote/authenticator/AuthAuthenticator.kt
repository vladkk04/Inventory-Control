package com.example.bachelorwork.data.remote.authenticator

import com.example.bachelorwork.data.constants.AppConstants.JWT_ACCESS_TOKEN_KEY
import com.example.bachelorwork.data.constants.AppConstants.JWT_REFRESH_TOKEN_KEY
import com.example.bachelorwork.data.remote.services.RefreshApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val refreshApiService: RefreshApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { dataStoreManager.getPreference(JWT_REFRESH_TOKEN_KEY).firstOrNull() }

        if (refreshToken == null) { return null }

        return runBlocking {
            val newToken = refreshApiService.refreshToken("Bearer $refreshToken")

            if(!newToken.isSuccessful || newToken.body() == null) {
                dataStoreManager.deletePreference(JWT_ACCESS_TOKEN_KEY)
                dataStoreManager.deletePreference(JWT_REFRESH_TOKEN_KEY)
                return@runBlocking null
            }

            newToken.body()?.let { response ->
                dataStoreManager.savePreference(JWT_ACCESS_TOKEN_KEY to response.token)
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
    }
}