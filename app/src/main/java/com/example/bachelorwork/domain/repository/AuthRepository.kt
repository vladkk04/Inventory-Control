package com.example.bachelorwork.domain.repository

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun signUp(email: String, password: String): Result<Unit>

    suspend fun authenticate(token: String)
}