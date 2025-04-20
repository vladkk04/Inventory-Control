package com.example.bachelorwork.data.remote.repositories

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.services.FileApiService
import com.example.bachelorwork.domain.model.file.FileResponse
import com.example.bachelorwork.domain.model.file.FolderType
import com.example.bachelorwork.domain.repository.remote.FileRepository
import com.example.bachelorwork.ui.utils.FileMimeType
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException

class FileRepositoryImpl(
    private val api: FileApiService,
    private val context: Context
): FileRepository {

    override suspend fun uploadFile(
        folderType: FolderType,
        uri: Uri,
        mimeType: FileMimeType?
    ): Flow<ApiResponseResult<FileResponse>> {
        return safeResponseApiCallFlow {
            val contentResolver = context.contentResolver
            val fileName = getFileName(contentResolver, uri)
            val mediaType = when {
                mimeType != null -> getMimeType(mimeType)
                uri.toString().endsWith(".jpg", ignoreCase = true) -> "image/jpeg"
                uri.toString().endsWith(".png", ignoreCase = true) -> "image/png"
                else -> contentResolver.getType(uri) ?: "application/octet-stream"
            }
            contentResolver.getType(uri) ?: "application/octet-stream"

            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? = mediaType.toMediaTypeOrNull()
                override fun contentLength(): Long = -1  // Don't need length for streaming

                override fun writeTo(sink: BufferedSink) {
                    contentResolver.openInputStream(uri)?.use { input ->
                        sink.writeAll(input.source())
                    } ?: throw IOException("Couldn't open input stream")
                }
            }

            val filePart = MultipartBody.Part.createFormData(
                "file",
                fileName,
                requestBody
            )

            api.uploadFile(folderType, filePart)
        }
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        return when (uri.scheme) {
            "content" -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    cursor.takeIf { it.moveToFirst() }?.let {
                        it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    } ?: uri.lastPathSegment ?: "unknown_${System.currentTimeMillis()}"
                } ?: uri.lastPathSegment ?: "unknown_${System.currentTimeMillis()}"
            }
            "file" -> uri.lastPathSegment ?: "unknown_${System.currentTimeMillis()}"
            else -> "unknown_${System.currentTimeMillis()}"
        }.also { name ->
            if (!name.contains('.')) {
                val extension = when (contentResolver.getType(uri)) {
                    "image/png" -> "png"
                    "image/jpeg" -> "jpg"
                    // Add other MIME types as needed
                    else -> "bin"
                }
                return "$name.$extension"
            }
        }
    }

    private fun getMimeType(mimeType: FileMimeType): String {
        return when (mimeType) {
            FileMimeType.PDF -> "application/pdf"
            FileMimeType.JPG, FileMimeType.JPEG, FileMimeType.PNG -> "image/*"
            FileMimeType.DOC -> "application/msword"
            FileMimeType.DOCX -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            FileMimeType.TXT -> "text/plain"
        }
    }

}