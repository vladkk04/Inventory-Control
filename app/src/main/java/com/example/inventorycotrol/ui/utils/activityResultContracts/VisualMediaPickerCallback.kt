package com.example.inventorycotrol.ui.utils.activityResultContracts

import android.net.Uri

fun interface VisualMediaPickerCallback {
    fun onMediaPicked(uri: Uri?)
}