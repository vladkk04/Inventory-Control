package com.example.bachelorwork.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bachelorwork.data.local.converters.DateConverter
import com.example.bachelorwork.data.local.converters.JsonOrderAttachment
import com.example.bachelorwork.data.local.converters.JsonOrderProduct
import com.example.bachelorwork.data.local.converters.JsonProductTagConverter
import com.example.bachelorwork.data.local.converters.JsonProductUpdateHistory
import com.example.bachelorwork.data.local.converters.UriConverter
import com.example.bachelorwork.data.local.dao.OrderDao
import com.example.bachelorwork.data.local.dao.OrganisationDao
import com.example.bachelorwork.data.local.dao.OrganisationUserDao
import com.example.bachelorwork.data.local.dao.ProductCategoryDao
import com.example.bachelorwork.data.local.dao.ProductDao
import com.example.bachelorwork.data.local.dao.UserDao
import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.data.local.entities.OrderProductJoin
import com.example.bachelorwork.data.local.entities.OrganisationEntity
import com.example.bachelorwork.data.local.entities.OrganisationUserEntity
import com.example.bachelorwork.data.local.entities.ProductCategoryEntity
import com.example.bachelorwork.data.local.entities.ProductEntity
import com.example.bachelorwork.data.local.entities.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        ProductCategoryEntity::class,
        OrderEntity::class,
        OrderProductJoin::class,
        OrganisationEntity::class,
        OrganisationUserEntity::class,
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class,
    JsonProductTagConverter::class,
    JsonOrderProduct::class,
    JsonOrderAttachment::class,
    JsonProductUpdateHistory::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val productCategoryDao: ProductCategoryDao
    abstract val orderDao: OrderDao
    abstract val organisationDao: OrganisationDao
    abstract val userDao: UserDao
    abstract val organisationUserDao: OrganisationUserDao

    companion object {
        private const val DATABASE_NAME = "app_db"

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}