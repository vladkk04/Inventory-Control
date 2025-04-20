package com.example.bachelorwork.domain.repository.remote

import android.net.Uri
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.file.FileResponse
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.ui.utils.FileMimeType
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    suspend fun uploadFile(
        folderType: FolderType,
        uri: Uri,
        mimeType: FileMimeType?
    ): Flow<ApiResponseResult<FileResponse>>
}