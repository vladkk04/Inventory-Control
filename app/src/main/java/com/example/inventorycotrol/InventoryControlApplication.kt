package com.example.inventorycotrol

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.notification.ProductStockNotificationService
import com.example.inventorycotrol.ui.worker.StockCheckWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class InventoryControlApplication: Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ProductStockNotificationService.PRODUCT_STOCK_CHANNEL_ID,
            "Product Stock",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for product stock notifications"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    class CustomWorkerFactory @Inject constructor(
        private val productApiService: ProductRemoteDataSource,
        private val notificationService: ProductStockNotificationService
    ): WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker = StockCheckWorker(appContext, workerParameters, productApiService, notificationService)
    }


}