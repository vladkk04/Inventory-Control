package com.example.inventorycotrol.data.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AppDownloader(context: Context) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadFile(url: String, title: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(title)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        return downloadManager.enqueue(request)
    }
}