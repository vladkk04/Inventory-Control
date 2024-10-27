package com.example.bachelorwork.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bachelorwork.domain.model.Product

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "products_db"
    }

    abstract val productDao: ProductDao
}