package com.example.bachelorwork.data.local

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bachelorwork.data.local.dao.OrderDao
import com.example.bachelorwork.data.local.dao.ProductCategoryDao
import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.data.local.converters.DateConverter
import com.example.bachelorwork.data.local.converters.JsonProductTagConverter
import com.example.bachelorwork.data.local.converters.JsonProductTimelineHistoryConverter
import com.example.bachelorwork.data.local.converters.UriConverter
import com.example.bachelorwork.data.local.entities.order.OrderEntity
import com.example.bachelorwork.data.local.entities.order.OrderProduct
import com.example.bachelorwork.data.local.entities.productCategory.ProductCategoryEntity
import com.example.bachelorwork.data.local.entities.product.ProductEntity
import com.example.bachelorwork.domain.model.product.DefaultCategories
import com.example.bachelorwork.util.names

@Database(
    entities = [
        ProductEntity::class,
        ProductCategoryEntity::class,
        OrderEntity::class,
        OrderProduct::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class,
    JsonProductTagConverter::class,
    JsonProductTimelineHistoryConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val productCategoryDao: ProductCategoryDao
    abstract val orderDao: OrderDao

    companion object {
        private const val DATABASE_NAME = "app_db"

        private val QUERY_INSERT_DEFAULT_PRODUCT_CATEGORIES = {
            val entries  = DefaultCategories.entries.names()
            .map { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
            .joinToString(", ") { "('$it')" }

            "INSERT INTO ${ProductCategoryEntity.TABLE_NAME} (name) VALUES $entries"
        }

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    try {
                        db.execSQL(QUERY_INSERT_DEFAULT_PRODUCT_CATEGORIES.invoke())
                    } catch (e: SQLiteException) {
                        e.printStackTrace()
                    }
                }
            }).build()
        }
    }
}