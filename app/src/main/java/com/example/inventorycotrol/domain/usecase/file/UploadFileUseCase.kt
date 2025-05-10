package com.example.inventorycotrol.domain.usecase.file

import android.net.Uri
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.repository.remote.FileRepository
import com.example.inventorycotrol.ui.utils.FileMimeType
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class UploadFileUseCase(
    private val repository: FileRepository
) {
    operator fun invoke(uri: Uri, mimeType: FileMimeType?) = flow {
        repository.uploadFile(uri, mimeType).last().let { response ->
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
