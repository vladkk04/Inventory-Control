package com.example.inventorycotrol.data.remote.services

import com.example.inventorycotrol.domain.model.file.FileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): Response<FileResponse>

}