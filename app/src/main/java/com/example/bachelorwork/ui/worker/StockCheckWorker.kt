package com.example.bachelorwork.ui.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.remote.mappers.mapToDomain
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.ui.notification.ProductStockNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.last

@HiltWorker
class StockCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val api: ProductRemoteDataSource,
    @Assisted private val notificationService: ProductStockNotificationService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = api.getAll().last()

            if(response is ApiResponseResult.Failure) {
                Result.failure()
            } else if(response is ApiResponseResult.Success) {
                val criticalProducts = response.data.filter { it.quantity < it.minStockLevel * 0.25 }.map { it.mapToDomain() }
                notificationService.showNotification(criticalProducts)
                Result.success()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

}