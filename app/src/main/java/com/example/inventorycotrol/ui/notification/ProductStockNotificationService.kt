package com.example.inventorycotrol.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.inventorycotrol.R
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.ui.MainActivity

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


        val notificationText = if (products.isNotEmpty()) {
            val itemsList = products.take(5).joinToString("\n") { product ->
                "• ${product.name}: Current ${product.quantity} (Min ${product.minStockLevel}) ${product.unit.name.lowercase()}"
            }
            val moreItemsText = if (products.size > 5) "\n+${products.size - 5} more items..." else ""
            "$itemsList$moreItemsText"
        } else {
            "All products are sufficiently stocked"
        }

        val notification = NotificationCompat.Builder(context, PRODUCT_STOCK_CHANNEL_ID)
            .setContentTitle(if (products.isNotEmpty()) "Low Stock Alert" else "Stock Levels Good")
            .setContentText(if (products.isNotEmpty()) "${products.size} items need restocking" else "No items need restocking")
            .setSmallIcon(if (products.isNotEmpty()) R.drawable.ic_trending_down else R.drawable.ic_done)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}