package com.example.bachelorwork.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bachelorwork.data.local.dao.ProductCategoryDao
import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.data.local.dao.converters.DateConverter
import com.example.bachelorwork.data.local.dao.converters.JsonProductTagConverter
import com.example.bachelorwork.data.local.dao.converters.UriConverter
import com.example.bachelorwork.data.local.entity.OrderEntity
import com.example.bachelorwork.data.local.entity.ProductCategoryEntity
import com.example.bachelorwork.data.local.entity.ProductEntity
import com.example.bachelorwork.data.local.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        ProductCategoryEntity::class,
        OrderEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class,
    JsonProductTagConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val productCategoryDao: ProductCategoryDao

    companion object {
        private const val DATABASE_NAME = "app_db"

        private val QUERY_INSERT_DEFAULT_PRODUCT_CATEGORIES = {
            val entries =
                ProductCategoryEntity.DefaultCategories.entries.joinToString { "('${it.name}')" }
            "INSERT INTO ${ProductCategoryEntity.TABLE_NAME} (name) VALUES $entries"
        }

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL(QUERY_INSERT_DEFAULT_PRODUCT_CATEGORIES.invoke())
                }
            }).build()
        }
    }
}