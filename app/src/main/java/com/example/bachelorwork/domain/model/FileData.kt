package com.example.bachelorwork.domain.model

data class FileData(
    val displayName: String? = null,
    val mimeType: FileMimeType? = null,
    val size: String? = null,
)

enum class FileMimeType {
    PDF,
    JPG,
    JPEG,
    PNG
}

