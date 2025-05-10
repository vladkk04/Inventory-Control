package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface UserApiService {

    @GET("/users/find")
    suspend fun getUserById(@Query("id") id: String): Response<UserDto>

    @GET("/users/find")
    suspend fun getUserByEmail(@Query("email") email: String): Response<UserDto>

}