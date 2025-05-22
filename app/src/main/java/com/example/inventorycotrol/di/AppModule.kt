package com.example.inventorycotrol.di

import android.content.Context
import com.example.inventorycotrol.data.observers.ApplicationConnectivityObserver
import com.example.inventorycotrol.domain.repository.ConnectivityObserver
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.local.AppDatabase
import com.example.inventorycotrol.data.remote.authenticator.AuthAuthenticator
import com.example.inventorycotrol.data.remote.interceptors.TokenInterceptor
import com.example.inventorycotrol.data.remote.interceptors.TokenInterceptorType
import com.example.inventorycotrol.data.remote.repositories.AuthRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.ProductRemoteDataImpl
import com.example.inventorycotrol.data.remote.services.AuthApiService
import com.example.inventorycotrol.data.remote.services.ProductApiService
import com.example.inventorycotrol.data.remote.services.RefreshApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.AppNavigatorImpl
import com.example.inventorycotrol.ui.notification.ProductStockNotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = AppDatabase.getInstance(applicationContext)

    @Provides
    @Singleton
    fun provideAppNavigator(
        dataStoreManager: DataStoreManager,
        authDataSource: AuthRemoteDataSource
    ): AppNavigator = AppNavigatorImpl(dataStoreManager, authDataSource)

    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        authApiService: AuthApiService,
        dataStoreManager: DataStoreManager,
        refreshApiService: RefreshApiService
    ): AuthRemoteDataSource = AuthRemoteDataSourceImpl(authApiService, refreshApiService, dataStoreManager)

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(
        productApiService: ProductApiService,
        dataStoreManager: DataStoreManager
    ): ProductRemoteDataSource = ProductRemoteDataImpl(productApiService, dataStoreManager)

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext applicationContext: Context
    ): ConnectivityObserver = ApplicationConnectivityObserver(applicationContext)

    @Provides
    @Singleton
    fun provideProductStockNotificationService(
        @ApplicationContext applicationContext: Context
    ): ProductStockNotificationService = ProductStockNotificationService(applicationContext)


    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAccessOkHttpClient(
        authTokenManager: DataStoreManager,
        refreshApiService: RefreshApiService
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .authenticator(AuthAuthenticator(authTokenManager, refreshApiService))
            .addInterceptor(TokenInterceptor(authTokenManager, TokenInterceptorType.ACCESS))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @TokenRefreshClient
    fun provideRefreshOkHttpClient(authTokenManager: DataStoreManager): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(authTokenManager, TokenInterceptorType.REFRESH))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpMainClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @AuthenticatedRetrofit
    fun provideAuthenticatedRetrofit(@AuthenticatedClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()

    @Provides
    @Singleton
    @TokenRefreshRetrofit
    fun provideTokenRefreshRetrofit(@TokenRefreshClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
