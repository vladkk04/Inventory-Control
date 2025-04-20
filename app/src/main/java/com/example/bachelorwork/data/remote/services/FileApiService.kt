package com.example.bachelorwork.data.remote.services

import com.example.bachelorwork.domain.model.file.FileResponse
import com.example.bachelorwork.domain.model.file.FolderType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FileApiService {

    @Multipart
    @POST("upload/{folderType}")
    suspend fun uploadFile(
        @Path("folderType") folderType: FolderType,
        @Part file: MultipartBody.Part
    ): Response<FileResponse>

}