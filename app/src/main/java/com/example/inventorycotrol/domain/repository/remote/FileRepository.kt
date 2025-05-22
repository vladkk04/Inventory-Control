package com.example.inventorycotrol.domain.repository.remote

import android.net.Uri
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.model.file.FileResponse
import com.example.inventorycotrol.domain.model.file.FileMimeType
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    suspend fun uploadFile(
        uri: Uri,
        mimeType: FileMimeType?
    ): Flow<ApiResponseResult<FileResponse>>
}