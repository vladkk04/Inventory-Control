package com.example.bachelorwork.data.remote.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bachelorwork.data.remote.auth.AuthApi
import com.example.bachelorwork.domain.model.auth.AuthRequest
import com.example.bachelorwork.domain.repository.AuthRepository
import kotlinx.coroutines.flow.collectLatest

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val preferences: DataStore<Preferences>
) : AuthRepository {

    private val jwtPreferencesKey = stringPreferencesKey("jwt")

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return runCatching {
            val response = api.signIn(AuthRequest(email, password))
            preferences.edit { preference ->
                preference[stringPreferencesKey("jwt")] = response.token
            }
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return runCatching { api.signUp(AuthRequest(email, password)) }
    }

    override suspend fun authenticate(token: String) {
        preferences.data.collectLatest { preferences ->
            api.authenticate(preferences[jwtPreferencesKey] ?: return@collectLatest)
        }
    }
}