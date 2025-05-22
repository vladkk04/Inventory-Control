package com.example.inventorycotrol.data.remote.repositories

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.services.FileApiService
import com.example.inventorycotrol.domain.model.file.FileResponse
import com.example.inventorycotrol.domain.repository.remote.FileRepository
import com.example.inventorycotrol.domain.model.file.FileMimeType
import com.example.inventorycotrol.ui.utils.extensions.safeSuspendResponseApiCallFlow
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

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8 * 1024
        private const val UNKNOWN_FILE_PREFIX = "unknown"
    }

    override suspend fun uploadFile(
        uri: Uri,
        mimeType: FileMimeType?
    ): Flow<ApiResponseResult<FileResponse>> = safeSuspendResponseApiCallFlow {
        val contentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val mediaType = determineMediaType(uri, mimeType, contentResolver)

        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = fileName,
            body = createRequestBody(contentResolver, uri, mediaType)
        )

        api.uploadFile(filePart)
    }

    private fun createRequestBody(
        contentResolver: ContentResolver,
        uri: Uri,
        mediaType: String
    ): RequestBody = object : RequestBody() {
        override fun contentType(): MediaType? = mediaType.toMediaTypeOrNull()

        override fun contentLength(): Long = getFileSize(contentResolver, uri)

        override fun writeTo(sink: BufferedSink) {
            contentResolver.openInputStream(uri)?.use { input ->
                input.source().use { source ->
                    sink.writeAll(source)
                }
            } ?: throw IOException("Couldn't open input stream")
        }
    }

    private fun determineMediaType(
        uri: Uri,
        mimeType: FileMimeType?,
        contentResolver: ContentResolver
    ): String = when {
        mimeType != null -> getMimeType(mimeType)
        uri.toString().endsWith(".jpg", ignoreCase = true) -> "image/jpeg"
        uri.toString().endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
        uri.toString().endsWith(".png", ignoreCase = true) -> "image/png"
        else -> contentResolver.getType(uri) ?: "application/octet-stream"
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        val (baseName, extension) = when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> getContentResolverFileName(contentResolver, uri)
            ContentResolver.SCHEME_FILE -> uri.lastPathSegment ?: ""
            else -> ""
        }.let { name ->
            if (name.contains('.')) {
                name to null
            } else {
                name to determineFileExtension(contentResolver, uri)
            }
        }

        return if (baseName.isNotEmpty()) {
            if (extension != null) "$baseName.$extension" else baseName
        } else {
            "$UNKNOWN_FILE_PREFIX${System.currentTimeMillis()}.${extension ?: "bin"}"
        }
    }

    private fun getContentResolverFileName(
        contentResolver: ContentResolver,
        uri: Uri
    ): String = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        } else {
            uri.lastPathSegment ?: ""
        }
    } ?: uri.lastPathSegment ?: ""

    private fun determineFileExtension(contentResolver: ContentResolver, uri: Uri): String? =
        when (contentResolver.getType(uri)) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            else -> null
        }

    private fun getFileSize(contentResolver: ContentResolver, uri: Uri): Long =
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getLong(cursor.getColumnIndexOrThrow(OpenableColumns.SIZE))
            } else {
                -1L
            }
        } ?: -1L

    private fun getMimeType(mimeType: FileMimeType): String = when (mimeType) {
        FileMimeType.PDF -> "application/pdf"
        FileMimeType.JPG -> "image/jpeg" // Fixed typo (was "image/jpg")
        FileMimeType.JPEG -> "image/jpeg"
        FileMimeType.PNG -> "image/png"
        FileMimeType.DOC -> "application/msword"
        FileMimeType.DOCX -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        FileMimeType.TXT -> "text/plain"
        FileMimeType.XLS -> "application/vnd.ms-excel"
        FileMimeType.XLSX -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }

}