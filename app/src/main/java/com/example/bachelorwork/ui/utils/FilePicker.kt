package com.example.bachelorwork.ui.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.example.bachelorwork.domain.model.FileData
import com.example.bachelorwork.domain.model.FileMimeType

class FilePicker {
    fun dumpImageMetaData(uri: Uri, contentResolver: ContentResolver): FileData {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        return cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumn = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val displayName = if (displayNameColumn != -1) {
                    it.getString(displayNameColumn) ?: "Unknown"
                } else {
                    "Unknown"
                }

                val sizeColumn = it.getColumnIndex(OpenableColumns.SIZE)

                val size = if (sizeColumn != -1 && !it.isNull(sizeColumn)) {
                    val sizeInBytes = it.getLong(sizeColumn)
                    formatFileSize(sizeInBytes)
                } else {
                    "Unknown"
                }

                val mimeType = contentResolver.getType(uri)

                FileData(displayName, mimeTypes[mimeType], size)

            } else { FileData() }
        } ?: FileData()
    }

    private val mimeTypes = mapOf(
        "application/pdf" to FileMimeType.PDF,
        "image/png" to FileMimeType.PNG,
        "image/jpg" to FileMimeType.JPG,
        "image/jpeg" to FileMimeType.JPG
    )

    private fun formatFileSize(sizeInBytes: Long): String =
        when {
            sizeInBytes < 1024 -> "$sizeInBytes B"
            sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
            sizeInBytes < 1024 * 1024 * 1024 -> "${sizeInBytes / (1024 * 1024)} MB"
            else -> "${sizeInBytes / (1024 * 1024 * 1024)} GB"
        }
}

