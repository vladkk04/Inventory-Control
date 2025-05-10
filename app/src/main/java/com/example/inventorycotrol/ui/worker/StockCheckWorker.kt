package com.example.inventorycotrol.ui.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.mappers.mapToDomain
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.notification.ProductStockNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.last
import java.util.Calendar
import kotlin.math.ceil

@HiltWorker
class StockCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val api: ProductRemoteDataSource,
    @Assisted private val notificationService: ProductStockNotificationService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val criticalThresholdPercentage =
                inputData.getDouble("critical_threshold_percentage", 25.00)
            val days =
                inputData.getIntArray("notification_days")?.toSet() ?: return Result.failure()

            val calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DAY_OF_WEEK)

            if (days.contains(today)) {
                val response = api.getAll().last()

                if (response is ApiResponseResult.Failure) {
                    return Result.failure()
                } else if (response is ApiResponseResult.Success) {
                    val criticalProducts = response.data.filter { product ->
                        product.quantity <= ceil(product.minStockLevel * (criticalThresholdPercentage / 100))
                    }.map { it.mapToDomain() }

                    notificationService.showNotification(criticalProducts)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}