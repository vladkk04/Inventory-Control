package com.example.inventorycotrol.domain.model.file

import android.net.Uri

data class FileData(
    val uri: Uri? = null,
    val displayName: String? = null,
    val mimeType: FileMimeType? = null,
    val size: String? = null,
)