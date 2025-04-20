package com.example.bachelorwork.domain.usecase.file

import android.net.Uri
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.domain.repository.remote.FileRepository
import com.example.bachelorwork.ui.utils.FileMimeType
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class UploadFileUseCase(
    private val repository: FileRepository
) {
    operator fun invoke(folderType: FolderType, uri: Uri, mimeType: FileMimeType?) = flow {
        repository.uploadFile(folderType, uri, mimeType).last().let { response ->
            when (response) {
                ApiResponseResult.Loading -> {}
                is ApiResponseResult.Failure -> {
                    emit(null)
                }
                is ApiResponseResult.Success -> {
                    emit(response.data.url)
                }
            }
        }
    }

}
