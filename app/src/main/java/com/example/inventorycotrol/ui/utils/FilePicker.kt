package com.example.inventorycotrol.ui.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.example.inventorycotrol.domain.model.file.FileData
import com.example.inventorycotrol.domain.model.file.FileMimeType

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
                FileData(uri, displayName, mimeTypes[mimeType], size)

            } else { FileData() }
        } ?: FileData()
    }

    private val mimeTypes = mapOf(
        "application/pdf" to FileMimeType.PDF,
        "image/png" to FileMimeType.PNG,
        "image/jpg" to FileMimeType.JPG,
        "image/jpeg" to FileMimeType.JPG,
        "application/msword" to FileMimeType.DOC,
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to FileMimeType.DOCX,
        "text/plain" to FileMimeType.TXT,
        "application/vnd.ms-excel" to FileMimeType.XLS,
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to FileMimeType.XLSX
    )

    private fun formatFileSize(sizeInBytes: Long): String =
        when {
            sizeInBytes < 1024 -> "$sizeInBytes B"
            sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
            sizeInBytes < 1024 * 1024 * 1024 -> "${sizeInBytes / (1024 * 1024)} MB"
            else -> "${sizeInBytes / (1024 * 1024 * 1024)} GB"
        }
}

