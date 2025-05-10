package com.example.inventorycotrol.ui.utils

import android.net.Uri

data class FileData(
    val uri: Uri? = null,
    val displayName: String? = null,
    val mimeType: FileMimeType? = null,
    val size: String? = null,
)

enum class FileMimeType {
    PDF,
    PNG,
    XLS,
    XLSX,
    JPG,
    JPEG,
    DOC,
    DOCX,
    TXT
}
