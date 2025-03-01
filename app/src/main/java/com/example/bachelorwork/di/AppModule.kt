package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.local.AppDatabase
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.AppNavigatorImpl
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.permissions.AppPermissionImpl
import com.example.bachelorwork.ui.permissions.PermissionController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = AppDatabase.getInstance(applicationContext)

    @Provides
    @Singleton
    fun providePermissionHandler(
        @ApplicationContext applicationContext: Context
    ): PermissionController {
        return AppPermissionImpl(applicationContext)
    }

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigatorImpl(Destination.Authentication)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.60:8080/")
        .addConverterFactory(
            Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()
            )
        ).build()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}