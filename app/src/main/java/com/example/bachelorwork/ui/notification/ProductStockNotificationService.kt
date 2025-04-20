package com.example.bachelorwork.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.bachelorwork.R
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.MainActivity

class ProductStockNotificationService(
    private val context: Context
): NotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val PRODUCT_STOCK_CHANNEL_ID = "product_stock_channel"
    }

    override fun showNotification(products: List<Product>) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val style = NotificationCompat.InboxStyle()
            .setSummaryText("Low stock products")

        products.take(7).forEach { product ->
            style.addLine("${product.name}: ${product.quantity} / ${product.minStockLevel} ${product.unit.name.lowercase()}")
        }

        val notification = NotificationCompat.Builder(context, PRODUCT_STOCK_CHANNEL_ID)
            .setContentTitle("Low stocks")
            .setContentText("${products.size} products need restocking")
            .setSmallIcon(R.drawable.ic_trending_down)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(style)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}