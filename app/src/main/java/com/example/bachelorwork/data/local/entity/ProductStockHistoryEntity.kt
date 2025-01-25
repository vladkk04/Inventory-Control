package com.example.bachelorwork.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = ProductStockHistoryEntity.TABLE_NAME)
data class ProductStockHistoryEntity(
    @PrimaryKey
    val id: Int,
    val productId: Int,
    val quantity: Int,
    val stockedAt: Date
) {
    companion object {
        const val TABLE_NAME = "product_stock_history"
    }
}
